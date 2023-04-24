package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.FragmentBasicAuthBinding
import com.google.gson.GsonBuilder
import org.json.JSONObject

class BasicAuthFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentBasicAuthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasicAuthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewUser.layoutManager = linearLayoutManager

        // Get data from url and show it on view
        binding.buttonGetDataBasicAuth.setOnClickListener {
            getUsers()
            // Hide recyclerView while loading
            binding.recyclerViewUser.visibility = View.GONE
            // Show loading spinner while loading
            binding.spinKit.visibility = View.VISIBLE
        }

        return root
    }

    private fun getUsers() {
        // this is the url where we want to get our data from
        val JSON_URL = BuildConfig.JSON_URL

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")

                var users = gson.fromJson(jsonArray.toString(), Array<User>::class.java).toList()

                adapter = UserAdapter(users)
                binding.recyclerViewUser.adapter = adapter

                // Hide spinner when url request is ok
                binding.spinKit.visibility = View.GONE
                // Make recyclerView visible in view
                binding.recyclerViewUser.visibility = View.VISIBLE

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)

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

                // replace with your own API's login info
                val authorizationString = "Basic " + Base64.encodeToString(
                    (BuildConfig.APING_WEB_BASIC_AUTH_USER_NAME + ":" +
                    BuildConfig.APING_WEB_BASIC_AUTH_PASSWORD).toByteArray(), Base64.DEFAULT
                )

                Log.d("TESTI", authorizationString)

                headers["Authorization"] = authorizationString;
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