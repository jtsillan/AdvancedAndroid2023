package com.example.em_tuntiesimerkki1.datatypes.cityweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.BuildConfig
import com.example.em_tuntiesimerkki1.R
import com.example.em_tuntiesimerkki1.databinding.FragmentCityMarkerBinding
import com.google.gson.GsonBuilder

/**
 * A simple [Fragment] subclass.
 * Use the [CityMarkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityMarkerFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentCityMarkerBinding? = null

    // Get fragment parameters from previous fragment
    val args: CityMarkerFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityMarkerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getWeather()

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    private fun getWeather() {
        // Haetaan API-key local.properties-tiedostosta
        val API_KEY : String = BuildConfig.OPEN_WEATHER_MAP_API_KEY
        val JSON_URL : String = "https://api.openweathermap.org/data/2.5/weather?lat=${args.latitude}&lon=${args.longitude}&appid=${API_KEY}&units=metric"

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)

                // Muunnetaan data CityWeather-olioksi
                var item: CityWeather = gson.fromJson(response, CityWeather::class.java)

                // Haetaan pelkkä lämpötila
                Log.d("ADVTECH", item.main?.temp.toString() + " C")

                // jos halutaan näyttää esim. TextViewissä ja sen id on textView_temperature:
                // binding.textViewTemperature.text = item.main?.temp.toString() + " C"
                binding.textViewTemperature.text = item.main?.temp.toString() + " C"
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        Log.d("ADVTECH", JSON_URL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}