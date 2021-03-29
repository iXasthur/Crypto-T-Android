package com.xcnk.cryptot.screens.home.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.xcnk.cryptot.R
import com.xcnk.cryptot.api.Session


class MapFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var mapIsReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_fragment_map) as SupportMapFragment
    }

    private fun setupViewObjects() {
        setupButtonListeners()

        mapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap
            mapIsReady = true
            updateMarkers()
        }
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }

    private fun updateMarkers() {
        Session.getLocalAssets()?.forEach { asset ->
            val event = asset.suggestedEvent
            if (event != null) {
                val pos = LatLng(event.latitude, event.longitude)
                googleMap.addMarker(MarkerOptions().position(pos).title(asset.name).snippet(event.note))
            }
        }
    }

}