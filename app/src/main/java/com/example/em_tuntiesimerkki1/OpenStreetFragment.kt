package com.example.em_tuntiesimerkki1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.example.em_tuntiesimerkki1.databinding.FragmentOpenStreetBinding
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


/**
 * A simple [Fragment] subclass.
 * Use the [OpenStreetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenStreetFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentOpenStreetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private lateinit var map : MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenStreetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context as Context))

        // binding-layerin kautta mapViewiin pääsy, ks. osm-droidin ohje miten lisätään ulkoasu tälle fragmentille
        // https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Kotlin)
        binding.map.setTileSource(TileSourceFactory.MAPNIK)

        // asetetaan alkupiste + zoomaus
        val mapController = binding.map.controller
        mapController.setZoom(18.0)
        val startPoint = GeoPoint(66.50352001528042, 25.727189822733095);
        mapController.setCenter(startPoint);

        // lisätään marker Rovaniemelle
        val marker = Marker(binding.map)
        marker.position = startPoint
        marker.title = "Rovaniemi marker!"
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        binding.map.overlays.add(marker)
        binding.map.invalidate()

        // onClick onnistuu mm. näin
        marker.setOnMarkerClickListener { marker, mapView ->

            Log.d("ADVTECH", "MARKKKERIIIII!")

            return@setOnMarkerClickListener false
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}