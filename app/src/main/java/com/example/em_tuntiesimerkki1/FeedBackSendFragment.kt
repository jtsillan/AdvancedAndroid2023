package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.FragmentFeedBackSendBinding
import com.example.em_tuntiesimerkki1.databinding.FragmentFeedbackReadBinding
import com.example.em_tuntiesimerkki1.datatypes.feedback.Feedback
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException

class FeedBackSendFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentFeedBackSendBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBackSendBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSendFeedbackToClient.setOnClickListener {
            var name = binding.editTextName.text.toString()
            var location = binding.editTextLocation.text.toString()
            var value = binding.editTextFeedbackValue.text.toString()

            Log.d("TESTI", "$name - $location - $value")

            sendFeedback(name, location, value)
        }

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    // Helper function to send new feedback with Volley
    private fun sendFeedback(name: String, location: String, value: String) {
        val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=${BuildConfig.DIRECTUS_ACCESS_TOKEN}"

        var gson = GsonBuilder().create();

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, JSON_URL,
            Response.Listener { response ->
                // usually APIs return the added new data back
                // when sending new data
                // therefore the response here should contain the JSON version
                // of the data you just sent below
                Log.d("TESTI", "Directusiin lähetys ok!")
                Toast.makeText(context, "Thanks for your feedback!", Toast.LENGTH_LONG).show()
                binding.editTextLocation.setText("")
                binding.editTextName.setText("")
                binding.editTextFeedbackValue.setText("")

                //activity?.supportFragmentManager?.popBackStack()

                val action = FeedBackSendFragmentDirections.actionFeedBackSendFragmentToFeedbackReadFragment()
                findNavController().navigate(action)
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise the API might block our queries!
                // define that we are after JSON data!
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }

            // let's build the new data here
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                // this function is only needed when sending data
                var body = ByteArray(0)
                try { // check the example "Converting a Kotlin object to JSON"
                    // on how to create this newData -variable
                    var newData = ""

                    var f : Feedback = Feedback()
                    f.name = name
                    f.location = location
                    f.value = value

                    newData = gson.toJson(f)

                    // create a new TodoItem object here, and convert it to string format (GSON)

                    // JSON to bytes
                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
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