package com.xcnk.cryptot.api

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.xcnk.cryptot.models.auth.AuthData
import com.xcnk.cryptot.models.crypto.CryptoAsset
import com.xcnk.cryptot.models.crypto.CryptoDashboard
import java.lang.Exception

object Session {

    private var authData: AuthData? = null
    private var dashboard: CryptoDashboard? = null

    private var initialized: Boolean = false

    private val cryptoAssetManager = CryptoAssetFirebaseManager()


    fun getLocalAssets(): ArrayList<CryptoAsset>? {
        return dashboard?.assets
    }

    fun getLocalAsset(id: String): CryptoAsset? {
        return getLocalAssets()?.first { asset ->
            asset.id == id
        }
    }

    private fun deleteLocalAsset(asset: CryptoAsset) {
        val index = getLocalAssets()?.indexOf(asset)
        if (index != null) {
            dashboard?.assets?.removeAt(index)
        }
    }

    fun deleteRemoteAsset(asset: CryptoAsset, completion: (Exception?) -> Unit) {
        cryptoAssetManager.deleteRemoteAsset(asset) { error ->
            if (error != null) {
                println(error)
                completion(error)
            } else {
                this.deleteLocalAsset(asset)
                completion(null)
            }
        }
    }

    private fun updateLocalAsset(asset: CryptoAsset) {
        val index = getLocalAssets()?.indexOf(asset)
        if (index != null) {
            dashboard?.assets?.set(index, asset)
        } else {
            dashboard?.assets?.add(asset)
        }
    }

    fun updateRemoteAsset(
        asset: CryptoAsset,
        iconUri: Uri?,
        videoUri: Uri?,
        completion: (Exception?) -> Unit
    ) {
        cryptoAssetManager.updateRemoteAsset(asset, iconUri, videoUri) { updatedAsset, error ->
            if (error != null) {
                println(error)
                completion(error)
            } else if (updatedAsset != null) {
                this.updateLocalAsset(updatedAsset)
                completion(null)
            } else {
                completion(Exception("Invalid updateRemoteAsset form CryptoAssetFirebaseManager closure return"))
            }
        }
    }

    fun syncDashboard(onCompleted: () -> Unit) {
        cryptoAssetManager.getRemoteAssets { assets, error ->
            if (error != null) {
                println(error)
                this.dashboard?.assets = ArrayList()
            } else if (assets != null) {
                this.dashboard?.assets = assets
            } else {
                println("Didn't receive assets and error")
                this.dashboard?.assets = ArrayList()
            }
            onCompleted()
        }
    }

    private fun initialize(authData: AuthData, onCompleted: () -> Unit) {
        this.authData = authData

        AuthDataStorage.save(authData)

        dashboard = CryptoDashboard()

        syncDashboard {
            this.initialized = true
            onCompleted()
        }

    }

    fun destroy() {
        initialized = false

        try {
            FirebaseAuth.getInstance().signOut()
        } catch (error: Throwable) {
            println(error)
        }

        authData = null
        dashboard = null
    }

    fun restore(completion: (Exception?) -> Unit): AuthData? {
        val authData = AuthDataStorage.restore()
        if (authData != null) {
            signInEmail(authData.email, authData.password) { error ->
                this.handleFirebaseAuthResponse(authData, error, completion)
            }
            return authData
        } else {
            completion(Exception("Unable to restore session"))
            return null
        }
    }

    fun signUpEmail(email: String, password: String, completion: (Exception?) -> Unit) {
        val authData = AuthData(email, password)
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { _ ->
                this.handleFirebaseAuthResponse(authData, null, completion)
            }
            .addOnFailureListener { e ->
                this.handleFirebaseAuthResponse(authData, e, completion)
            }
    }

    fun signInEmail(email: String, password: String, completion: (Exception?) -> Unit) {
        val authData = AuthData(email, password)
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _ ->
                this.handleFirebaseAuthResponse(authData, null, completion)
            }
            .addOnFailureListener { e ->
                this.handleFirebaseAuthResponse(authData, e, completion)
            }
    }

    private fun handleFirebaseAuthResponse(authData: AuthData, error: Exception?, completion: (Exception?) -> Unit) {
        if (error != null) {
            completion(error)
            return
        }

        initialize(authData) {
            if (this.initialized) {
                completion(null)
            } else {
                completion(Exception("Unable to initialize session"))
            }
        }
    }

}