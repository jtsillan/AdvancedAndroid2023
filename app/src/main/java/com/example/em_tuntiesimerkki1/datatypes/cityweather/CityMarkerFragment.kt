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


class CityMarkerFragment : Fragment() {

    private var _binding: FragmentCityMarkerBinding? = null

    // Get fragment parameters from previous fragment
    private val args: CityMarkerFragmentArgs by navArgs()

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

        return root
    }

    private fun getWeather() {
        // Get API-key from local.properties -file
        val API_KEY : String = BuildConfig.OPEN_WEATHER_MAP_API_KEY
        val JSON_URL : String = "https://api.openweathermap.org/data/2.5/weather?lat=${args.latitude}&lon=${args.longitude}&appid=${API_KEY}&units=metric"

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->

                // Change data to CityWeather object
                val item: CityWeather = gson.fromJson(response, CityWeather::class.java)

                // Show CityWeather class items in view
                binding.textViewCityName.text = item.name.toString() + " weather:"
                binding.textViewHumidity.text = "Humidity: " + item.main?.humidity.toString() + " %"
                binding.textViewTemperature.text = "Temperature: " + item.main?.temp.toString() + " C"
                binding.textViewWindSpeed.text = "Wind speed: " + item.wind?.speed.toString() + " m/s"
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}