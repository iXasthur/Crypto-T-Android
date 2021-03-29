package com.xcnk.cryptot.screens.creator

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.Session
import com.xcnk.cryptot.models.crypto.CryptoAsset
import com.xcnk.cryptot.models.crypto.CryptoEvent
import com.xcnk.cryptot.utils.startAnimation
import com.xcnk.cryptot.utils.stopAnimation
import java.util.*


class CreatorFragment : Fragment() {

    private var assetToEdit: CryptoAsset? = null

    private lateinit var nameEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var descriptionEditText: EditText

    private lateinit var iconImageView: ImageView
    private lateinit var selectIconButton: Button
    private lateinit var deleteIconButton: Button
    private var iconUri: Uri? = null

    private lateinit var videoView: VideoView
    private lateinit var selectVideoButton: Button
    private lateinit var deleteVideoButton: Button
    private var videoUri: Uri? = null

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

        iconImageView = view.findViewById(R.id.fragment_creator_icon)
        selectIconButton = view.findViewById(R.id.fragment_creator_button_select_icon)
        deleteIconButton = view.findViewById(R.id.fragment_creator_button_delete_icon)

        videoView = view.findViewById(R.id.fragment_creator_video)
        selectVideoButton = view.findViewById(R.id.fragment_creator_button_select_video)
        deleteVideoButton = view.findViewById(R.id.fragment_creator_button_delete_video)

        progressBar = view.findViewById(R.id.fragment_creator_progress_bar)
    }

    private fun setupViewObjects() {
        setupButtonListeners()

        assetToEdit = Session.selectedAsset
        if (assetToEdit != null) {
            nameEditText.setText(assetToEdit!!.name)
            codeEditText.setText(assetToEdit!!.code)
            descriptionEditText.setText(assetToEdit!!.description)

            val vURL = assetToEdit!!.videoFileData?.downloadURL
            if (vURL != null) {
                videoUri = Uri.parse(vURL)
            }

            val iURL = assetToEdit!!.iconFileData?.downloadURL
            if (iURL != null) {
                iconUri = Uri.parse(iURL)
            }
        }

        syncIcon()
        syncVideo()
        progressBar.visibility = View.INVISIBLE
    }

    private fun setupButtonListeners() {
        selectIconButton.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            i.putExtra("crop", "true")
            i.putExtra("aspectX", 100)
            i.putExtra("aspectY", 100)
            i.putExtra("outputX", 200)
            i.putExtra("outputY", 200)

            try {
                i.putExtra("return-data", true)
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 0)
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }

        deleteIconButton.setOnClickListener {
            iconUri = null
            syncIcon()
        }

        selectVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            intent.type = "video/*"
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 1)
        }

        deleteVideoButton.setOnClickListener {
            videoUri = null
            syncVideo()
        }
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
//                    val event: CryptoEvent? = null
//                    if let eventLatitude = eventLatitude,
//                    let eventLongitude = eventLongitude {
//                        event = CryptoEvent(note: eventNote, latitude: eventLatitude, longitude: eventLongitude)
//                    } else {
//                        event = nil
//                    }

                    val asset = CryptoAsset(
                        assetToEdit?.id ?: UUID.randomUUID().toString(),
                        nameEditText.text.toString(),
                        codeEditText.text.toString(),
                        descriptionEditText.text.toString(),
                        assetToEdit?.iconFileData,
                        assetToEdit?.videoFileData,
                        null
                    )

                    startAnimation(progressBar)
                    Session.updateRemoteAsset(asset, iconUri, videoUri) { error ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                if (data != null) {
                    iconUri = data.data
                    syncIcon()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                if (data != null) {
                    videoUri = data.data
                    syncVideo()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun syncIcon() {
        if (iconUri != null) {
            Picasso.get()
                    .load(iconUri)
                    .placeholder(R.drawable.ic_cryptos)
                    .error(R.drawable.ic_cryptos)
                    .into(iconImageView)
            iconImageView.visibility = View.VISIBLE
        } else {
            iconImageView.visibility = View.GONE
        }
    }

    private fun syncVideo() {
        if (videoUri != null) {
            videoView.setVideoURI(videoUri)
            videoView.setMediaController(MediaController(requireContext()))
            videoView.seekTo(100)
            videoView.visibility = View.VISIBLE
        } else {
            videoView.visibility = View.GONE
        }
    }
}