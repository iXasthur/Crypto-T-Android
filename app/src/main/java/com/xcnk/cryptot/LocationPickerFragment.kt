package com.xcnk.cryptot

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.xcnk.cryptot.api.Session
import com.xcnk.cryptot.screens.locationPicker.LocationPickerActivity


class LocationPickerFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var mapIsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_picker, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_location_picker_fragment_map) as SupportMapFragment
    }

    private fun setupViewObjects() {
        mapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap
            mapIsReady = true
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
                LocationPickerActivity.lastPickedLocation = googleMap.cameraPosition.target
                println(LocationPickerActivity.lastPickedLocation)
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}