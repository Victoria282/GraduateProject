package com.example.graduateproject.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentMapsBinding
import com.example.graduateproject.maps.model.Place
import com.example.graduateproject.utils.Utils.vectorToBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapsFragment @Inject constructor(

) : Fragment(R.layout.fragment_maps) {

    @Inject
    lateinit var placesReader: PlacesReader

    private lateinit var binding: FragmentMapsBinding

    private val places: List<Place> by lazy {
        placesReader.read()
    }

    private val studyIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.drawable_icon)
        vectorToBitmap(requireContext(), R.drawable.ic_study, color)
    }

    private val residenceIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.drawable_icon_light)
        vectorToBitmap(requireContext(), R.drawable.ic_home, color)
    }

    private val libraryIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.drawable_icon_light)
        vectorToBitmap(requireContext(), R.drawable.ic_book, color)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        places.forEach { place ->
            val icon = chooseIcon(place.typeMapPlace)
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.tittle)
                    .position(LatLng(place.latitude, place.longitude))
                    .snippet(place.description)
            )?.setIcon(
                icon
            )
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chelyabinsk, 10f))
    }

    private fun chooseIcon(typeOfPlace: Int): BitmapDescriptor? {
        return when (typeOfPlace) {
            0 -> studyIcon
            1 -> residenceIcon
            2 -> libraryIcon
            else -> null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        private val chelyabinsk = LatLng(55.154, 61.4291)
    }
}