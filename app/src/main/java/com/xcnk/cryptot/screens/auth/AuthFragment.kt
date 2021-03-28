package com.xcnk.cryptot.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.xcnk.cryptot.R


class AuthFragment : Fragment() {

    private lateinit var signUpButton: Button
    private lateinit var signInButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        signInButton = view.findViewById(R.id.fragment_auth_button_sign_in)
        signUpButton = view.findViewById(R.id.fragment_auth_button_sign_up)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        signInButton.setOnClickListener {

        }

        signUpButton.setOnClickListener {

        }
    }

}