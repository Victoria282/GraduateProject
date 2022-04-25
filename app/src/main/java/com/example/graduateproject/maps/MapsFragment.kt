package com.example.graduateproject.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.graduateproject.R
import com.example.graduateproject.databinding.FragmentMapsBinding
import com.example.graduateproject.maps.model.Place
import com.example.graduateproject.utils.Utils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MapsFragment @Inject constructor() : Fragment(R.layout.fragment_maps) {

    @Inject
    lateinit var placesReader: PlacesReader

    private lateinit var binding: FragmentMapsBinding
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private val places: List<Place> by lazy {
        placesReader.read()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        places.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.tittle)
                    .position(LatLng(place.latitude, place.longitude))
                    .snippet(place.description)
                    .icon(Utils.vectorToBitmap(requireContext(), place.getIcon()))
            )
        }
        currentLocation?.let {
            val currentLocation = LatLng(it.latitude, it.longitude)
            val makerOptions = MarkerOptions().position(currentLocation)
                .icon(Utils.vectorToBitmap(requireContext(), R.drawable.map_marker_red))
                .title(getString(R.string.tittle_map_my_location))
            googleMap.addMarker(makerOptions)
        }
        googleMap.mapType
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chelyabinsk, 10f))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMap()
        initListeners()
    }

    private fun loadMap() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val task = fusedLocationProvider.lastLocation
            task.addOnSuccessListener { location ->
                currentLocation = location
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            }
        } else {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            permissionLauncher.launch(permissions)
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        if (permission["android.permission.ACCESS_FINE_LOCATION"] == true
            &&
            permission["android.permission.ACCESS_COARSE_LOCATION"] == true
        )
            loadMap()
    }

    private fun initListeners() = with(binding) {
        corpusFilter.setOnClickListener {
            changeStateButton(it)
        }

        libraryFilter.setOnClickListener {
            changeStateButton(it)
        }

        dormitoryFilter.setOnClickListener {
            changeStateButton(it)
        }
    }

    private fun changeStateButton(view: View) {
        val color = if(view.isSelected) R.color.button_clicked else R.color.button_not_clicked
        view.setBackgroundColor(resources.getColor(color))
        view.isSelected = !view.isSelected
    }

    companion object {
        private val chelyabinsk = LatLng(55.154, 61.4291)
    }
}