package com.xcnk.cryptot.screens.creator

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ProgressBar
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.Session
import com.xcnk.cryptot.models.crypto.CryptoAsset
import com.xcnk.cryptot.utils.startAnimation
import com.xcnk.cryptot.utils.stopAnimation
import java.util.*


class CreatorFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var descriptionEditText: EditText

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_creator, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        nameEditText = view.findViewById(R.id.fragment_creator_name)
        codeEditText = view.findViewById(R.id.fragment_creator_code)
        descriptionEditText = view.findViewById(R.id.fragment_creator_description)
        progressBar = view.findViewById(R.id.fragment_creator_progress_bar)
    }

    private fun setupViewObjects() {
        setupButtonListeners()

        progressBar.visibility = View.INVISIBLE
    }

    private fun setupButtonListeners() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_creator_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            R.id.menu_creator_confirm -> {
                if (validateInput()) {
                    val asset = CryptoAsset(
                        UUID.randomUUID().toString(),
                        nameEditText.text.toString(),
                        codeEditText.text.toString(),
                        descriptionEditText.text.toString(),
                        null,
                        null,
                        null
                    )

                    startAnimation(progressBar)
                    Session.updateRemoteAsset(asset, null, null) { error ->
                        stopAnimation(progressBar)
                        if (error == null) {
                            requireActivity().onBackPressed()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInput(): Boolean {
        var ret = true
        val name = nameEditText.text.toString()
        val code = codeEditText.text.toString()
        val description = descriptionEditText.text.toString()
        if (name.isEmpty()) {
            nameEditText.error = requireContext().getString(R.string.must_be_not_empty)
            ret = false
        }
        if (code.isEmpty()) {
            codeEditText.error = requireContext().getString(R.string.must_be_not_empty)
            ret = false
        }
        if (description.isEmpty()) {
            descriptionEditText.error = requireContext().getString(R.string.must_be_not_empty)
            ret = false
        }
        return ret
    }
}