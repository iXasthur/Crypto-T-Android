package com.xcnk.cryptot.screens.cryptoDetails

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.Session
import de.hdodenhof.circleimageview.CircleImageView


class CryptoDetailsFragment : Fragment() {

    private lateinit var iconView: CircleImageView
    private lateinit var nameTextView: TextView
    private lateinit var codeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var videoView: VideoView
    private lateinit var videoStackView: LinearLayout
    private lateinit var eventLatitudeTextView: TextView
    private lateinit var eventLongitudeTextView: TextView
    private lateinit var eventNoteTextView: TextView
    private lateinit var eventStackView: LinearLayout

    override fun onResume() {
        super.onResume()
        syncWithSelected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crypto_details, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        iconView = view.findViewById(R.id.fragment_crypto_details_icon)
        nameTextView = view.findViewById(R.id.fragment_crypto_details_text_name)
        codeTextView = view.findViewById(R.id.fragment_crypto_details_text_code)
        descriptionTextView = view.findViewById(R.id.fragment_crypto_details_text_description)
        videoView = view.findViewById(R.id.fragment_crypto_details_video)
        videoStackView = view.findViewById(R.id.fragment_crypto_details_video_stack)
        eventLatitudeTextView = view.findViewById(R.id.fragment_crypto_details_text_event_latitude)
        eventLongitudeTextView = view.findViewById(R.id.fragment_crypto_details_text_event_longitude)
        eventNoteTextView = view.findViewById(R.id.fragment_crypto_details_text_event_note)
        eventStackView = view.findViewById(R.id.fragment_crypto_details_event_stack)
    }

    private fun setupViewObjects() {
        videoView.setMediaController(MediaController(requireContext()));
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_crypto_details_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            R.id.menu_crypto_details_edit -> {
                findNavController().navigate(R.id.action_cryptoDetailsFragment_to_creatorActivity2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun syncWithSelected() {
        val asset = Session.selectedAsset
        if (asset != null) {
            if (asset.iconFileData != null) {
                Picasso.get()
                    .load(asset.iconFileData!!.downloadURL)
                    .placeholder(R.drawable.ic_cryptos)
                    .error(R.drawable.ic_cryptos)
                    .into(iconView)
            } else {
                iconView.setImageResource(R.drawable.ic_cryptos)
            }

            nameTextView.text = asset.name
            codeTextView.text = asset.code
            descriptionTextView.text = asset.description

            if (asset.videoFileData != null) {
                videoView.setVideoPath(asset.videoFileData!!.downloadURL)
                videoView.seekTo(100)
                videoStackView.visibility = View.VISIBLE
            } else {
                videoStackView.visibility = View.GONE
            }

            if (asset.suggestedEvent != null) {
                eventLatitudeTextView.text = asset.suggestedEvent!!.latitude.toString()
                eventLongitudeTextView.text = asset.suggestedEvent!!.longitude.toString()
                eventNoteTextView.text = asset.suggestedEvent!!.note
                eventStackView.visibility = View.VISIBLE
            } else {
                eventStackView.visibility = View.GONE
            }
        }
    }
}