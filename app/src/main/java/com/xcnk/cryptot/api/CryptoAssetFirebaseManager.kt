package com.xcnk.cryptot.api

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.xcnk.cryptot.models.crypto.CloudFileData
import com.xcnk.cryptot.models.crypto.CryptoAsset
import com.xcnk.cryptot.utils.Constants
import com.xcnk.cryptot.utils.GsonConverter
import java.io.InputStream
import java.lang.Exception

class CryptoAssetFirebaseManager {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun deleteRemoteAsset(asset: CryptoAsset, completion: (Exception?) -> Unit) {

        val iconFile = asset.iconFileData
        if (iconFile != null) {
            deleteFile(iconFile) { error ->
                if (error != null) {
                    println(error)
                } else {
                    println("Deleted file with path $iconFile.path")
                }
            }
        }

        val videoFile = asset.videoFileData
        if (videoFile != null) {
            deleteFile(videoFile) { error ->
                if (error != null) {
                    println(error)
                } else {
                    println("Deleted file with path $videoFile.path")
                }
            }
        }

        val document = db.collection(Constants.Api.Firebase.assetsCollectionName).document(asset.id)
        document
            .delete()
            .addOnSuccessListener {
                completion(null)
            }
            .addOnFailureListener { e ->
                completion(e)
            }
    }

    private fun getStorageDownloadURL(path: String, completion: (Uri?, Exception?) -> Unit) {
        val storageRef = storage.reference
        val fileRef = storageRef.child(path)
        fileRef
            .downloadUrl
            .addOnSuccessListener { uri ->
                completion(uri, null)
            }
            .addOnFailureListener { e ->
                completion(null, e)
            }
    }

    private fun deleteFile(file: CloudFileData, completion: (Exception?) -> Unit) {
        val storageRef = this.storage.reference
        val fileRef = storageRef.child(file.path)

        fileRef
            .delete()
            .addOnSuccessListener {
                completion(null)
            }
            .addOnFailureListener { e ->
                completion(e)
            }
    }

    private fun uploadFile(fileRef: StorageReference, stream: InputStream, metadata: StorageMetadata, completion: (CloudFileData?, Exception?) -> Unit) {
        fileRef
            .putStream(stream, metadata)
            .addOnSuccessListener { _ ->
                this.getStorageDownloadURL(fileRef.path) { uri, error ->
                    when {
                        error != null -> {
                            completion(null, error)
                        }
                        uri != null -> {
                            completion(CloudFileData(fileRef.path, uri.toString()), null)
                        }
                        else -> {
                            completion(null, Exception("Both uri and error in uploadFile are null"))
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                completion(null, e)
            }
    }

    private fun uploadImage(imageUri: Uri, completion: (CloudFileData?, Exception?) -> Unit) {
        try {
            TODO()
        } catch (error: Throwable) {
            completion(null, Exception("Unable to process image Uri"))
        }
//        do {
//            let url = iconNSURL as URL
//                    let imageData = try Data(contentsOf: url)
//
//                if let image = UIImage(data: imageData)?.cropedToSquare()?.resizeImage(128, opaque: true) {
//                    if let data = image.jpegData(compressionQuality: 1) {
//                    let storageRef = storage.reference()
//                    let path = "\(Constants.imagesFolderFirebaseName)/\(UUID().uuidString).jpeg"
//                    let imageRef = storageRef.child(path)
//
//                    let metadata = StorageMetadata()
//                    metadata.contentType = "image/jpeg"
//
//                    uploadFile(fileRef: imageRef, data: data, metadata: metadata) { (fileData, error) in
//                        completion(fileData, error)
//                    }
//
//                } else {
//                    completion(nil, NSError.withLocalizedDescription("Unable to get png data from image"))
//                }
//                } else {
//                    completion(nil, NSError.withLocalizedDescription("Unable to optimize image size and form"))
//                }
//            } catch {
//                completion(nil, NSError.withLocalizedDescription("Unable to process image NSURL"))
//            }
        }

    private fun uploadVideo(videoUri: Uri, completion: (CloudFileData?, Exception?) -> Unit) {
        try {
            TODO()
        } catch (error: Throwable) {
            completion(null, Exception("Unable to process video Uri"))
        }
//        if let url = videoNSURL.absoluteURL {
//            let avAsset = AVURLAsset(url: url)
//            avAsset.exportVideo { (url) in
//                    if let url = url {
//                        do {
//                            let nsdata = try NSData(contentsOf: url, options: .mappedIfSafe)
//                                let data = Data(referencing: nsdata)
//
//                                let storageRef = self.storage.reference()
//                                let path = "\(Constants.videosFolderFirebaseName)/\(UUID().uuidString).mp4"
//                                let imageRef = storageRef.child(path)
//
//                                let metadata = StorageMetadata()
//                                metadata.contentType = "video/mp4"
//
//                                self.uploadFile(fileRef: imageRef, data: data, metadata: metadata) { (fileData, error) in
//                                    completion(fileData, error)
//                                }
//                            } catch {
//                                completion(nil, NSError.withLocalizedDescription("Unable to convert video URL"))
//                            }
//                        } else {
//                        completion(nil, NSError.withLocalizedDescription("Unable to convert video"))
//                    }
//            }
//        } else {
//            completion(nil, NSError.withLocalizedDescription("Unable to get video URL"))
//        }
    }

    private fun uploadAsset(asset: CryptoAsset, completion: (Exception?) -> Unit) {
        try {
            val map = GsonConverter.toMap(asset)?.toMutableMap()
            map?.remove("id")

            if (map != null) {
                val document = db.collection(Constants.Api.Firebase.assetsCollectionName).document(asset.id)
                document
                    .set(map)
                    .addOnSuccessListener {
                        println("Document successfully written!")
                        completion(null)
                    }
                    .addOnFailureListener { e ->
                        println("Error writing document: $e")
                        completion(e)
                    }
            } else {
                completion(Exception("Unable to create json object"))
            }
        } catch (_: Throwable) {
            completion(Exception("Unable to encode crypto asset"))
        }
    }

    private fun updateRemoteAssetRec(asset: CryptoAsset, iconUri: Uri?, videoUri: Uri?, completion: (CryptoAsset?, Exception?) -> Unit) {

        // Upload files 1 by 1 with every call
        // Priority:
        // 1 - Video
        // 2 - Image
        // 3 - Asset

        if (asset.videoFileData?.downloadURL != videoUri?.path) {
            var updatedAsset = asset

            // Delete previous file in background
            val videoFileData = asset.videoFileData
            if (videoFileData != null) {
                updatedAsset.videoFileData = null
                deleteFile(videoFileData) { error ->
                    if (error != null) {
                        print(error)
                    } else {
                        print("Deleted icon with path $videoFileData.path")
                    }
                }
            }

            // Upload with recursive call in completion
            if (videoUri != null) {
                uploadVideo(videoUri) { fileData, error ->
                    if (error != null) {
                        // Error uploading video so we ignore it
                        println(error)
                        this.updateRemoteAssetRec(updatedAsset, iconUri, null, completion)
                    } else if (fileData != null) {
                        // Successful video upload
                        updatedAsset.videoFileData = fileData

                        val downloadUri = Uri.parse(fileData.downloadURL)
                        this.updateRemoteAssetRec(updatedAsset, iconUri, downloadUri, completion)
                    }
                }
            } else {
                this.updateRemoteAssetRec(updatedAsset, iconUri, videoUri, completion)
            }

            return
        }


        if (asset.iconFileData?.downloadURL != iconUri?.path) {
            var updatedAsset = asset

            // Delete previous file in background
            val iconFileData = asset.iconFileData
            if (iconFileData != null) {
                updatedAsset.iconFileData = null
                deleteFile(iconFileData) { error ->
                        if (error != null) {
                            println(error)
                        } else {
                            println("Deleted icon with path $iconFileData.path")
                        }
                }
            }

            // Upload with recursive call in completion
            if (iconUri != null) {
                uploadImage(iconUri) { fileData, error ->
                    if (error != null) {
                        // Error uploading video so we ignore it
                        println(error)
                        this.updateRemoteAssetRec(updatedAsset, null, videoUri, completion)
                    } else if (fileData != null) {
                        // Successful video upload
                        updatedAsset.iconFileData = fileData

                        val downloadUri = Uri.parse(fileData.downloadURL)
                        this.updateRemoteAssetRec(updatedAsset, downloadUri, videoUri, completion)
                    }
                }
            } else {
                this.updateRemoteAssetRec(updatedAsset, iconUri, videoUri, completion)
            }

            return
        }

        uploadAsset(asset) { error ->
                if (error != null) {
                    completion(null, error)
                } else {
                    completion(asset, null)
                }
        }

    }

    fun updateRemoteAsset(asset: CryptoAsset, iconUri: Uri?, videoUri: Uri?, completion: (CryptoAsset?, Exception?) -> Unit) {
        updateRemoteAssetRec(asset, iconUri, videoUri) { updatedAsset, error ->
            completion(updatedAsset, error)
        }
    }

    fun getRemoteAssets(completion: (ArrayList<CryptoAsset>?, Exception?) -> Unit) {
        db.collection(Constants.Api.Firebase.assetsCollectionName)
            .get()
            .addOnSuccessListener { query ->
                val assets: ArrayList<CryptoAsset> = ArrayList()
                query.documents.forEach { document ->
                    val jsonData = document.data
                    jsonData?.set("id", document.id)

                    val asset: CryptoAsset? = GsonConverter.toObj(GsonConverter.toStr(jsonData))
                    if (asset != null) {
                        assets.add(asset)
                    } else {
                        println("Can't convert Firebase data to CryptoAsset")
                    }
                }
                completion(assets, null)
            }
            .addOnFailureListener { e ->
                completion(null, e)
            }
    }

}