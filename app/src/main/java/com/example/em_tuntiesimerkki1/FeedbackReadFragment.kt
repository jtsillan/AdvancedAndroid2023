package com.example.em_tuntiesimerkki1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.FragmentFeedbackReadBinding
import com.example.em_tuntiesimerkki1.datatypes.feedback.Feedback
import com.google.gson.GsonBuilder
import org.json.JSONObject

class FeedbackReadFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentFeedbackReadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: FeedbackAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Create layout manager and bind its view to recyclerview
        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewFeedbackDetail.layoutManager = linearLayoutManager

        // the binding -object allows you to access views in the layout, textviews etc.
        binding.buttonFetchData.setOnClickListener {
            getFeedback()
        }

        binding.buttonSendFeedbackFragment.setOnClickListener {
            val action = FeedbackReadFragmentDirections.actionFeedbackReadFragmentToFeedBackSendFragment()
            it.findNavController().navigate(action)
        }

        return root
    }


    private fun getFeedback() {
        // this is the url where we want to get our data from
        val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=${BuildConfig.DIRECTUS_ACCESS_TOKEN}"

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")

                var feedbacks = gson.fromJson(jsonArray.toString(), Array<Feedback>::class.java).toList()
//
                adapter = FeedbackAdapter(feedbacks as MutableList<Feedback>)
                binding.recyclerViewFeedbackDetail.adapter = adapter
//
                for (item in feedbacks) {
                    Log.d("ADVTECH", item.name.toString())
                }
//
//                val adapter = ArrayAdapter(context as Context, android.R.layout.simple_list_item_1, feedbacks)
//                binding.recyclerViewFeedbackDetail.adapter = adapter

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)
                Log.d("ADVTECH", jsonArray.toString())

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

