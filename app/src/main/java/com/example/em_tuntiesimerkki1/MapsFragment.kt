package com.example.em_tuntiesimerkki1

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.em_tuntiesimerkki1.databinding.FragmentDataReadBinding
import com.example.em_tuntiesimerkki1.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    // change this to match your fragment name
    private var _binding: FragmentMapsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        // laitaan googleMap-olio taltaan loukan tasolle (gMap)
        gMap = googleMap

        // käytetään MapsFragmentissa olevaa onMarkerClick-funkitiota jos markeria klikataan
        googleMap.setOnMarkerClickListener(this)

        val sydney = LatLng(-34.0, 151.0)
        var marker1 : Marker? = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        marker1?.tag = "Sydney"
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val rovaniemi = LatLng(66.50554904111243, 25.72843758748092)
        var marker2 : Marker? = googleMap.addMarker(MarkerOptions().position(rovaniemi).title("Marker in Rovaniemi"))
        marker2?.tag = "Rovaniemi"

        // newLatLongZoom avulla voidaan siirtää kameraa ja myös zoomata
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rovaniemi, 15f))
    }

    // Jotta pääsemme GoogleMaps-objektiin käsiksi myöhemmin
    private lateinit var gMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.checkBoxZoomController.setOnCheckedChangeListener { buttonView, isChecked ->
            gMap.uiSettings.isZoomControlsEnabled = isChecked
        }

        // Radio button -> muutetaan kartan tyyppi VAIN SILLOIN, jos
        // radio button on valittu (oletuksena setOnChecked käynnistyy molemmissa
        // tapauksessa, eli on tai off, mikä tuo bugeja tähän tilanteeseen
        binding.radioButtonNormalMap.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        // sama hybrid-kartalle
        binding.radioButtonHybridMap.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    // jos mitä tahansa markkeria klikataan, ajetaan tämä funktio
    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("ADVTECH", "MARKKERI")

        // Lokitetaan/tulostetaan markeriin liitetyt koordinaatit sekä
        // tägi, jossa pitäisi olla kaupingin nimi
        Log.d("ADVTECH", p0.position.latitude.toString())
        Log.d("ADVTECH", p0.position.longitude.toString())
        Log.d("ADVTECH", p0.tag.toString())

        // Navigate to CityWeatherFragment and pass coordinates
        val action = MapsFragmentDirections.actionMapsFragmentToCityMarkerFragment(
            p0.position.latitude.toFloat(), p0.position.longitude.toFloat()
        )
        findNavController().navigate(action)

        // onMarkerClick vaatii lopussa että palautetaan boolean
        return true
    }
}