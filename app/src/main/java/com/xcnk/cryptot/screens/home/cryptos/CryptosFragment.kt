package com.xcnk.cryptot.screens.home.cryptos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcnk.cryptot.R

class CryptosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cryptos, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {

    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {

    }

}