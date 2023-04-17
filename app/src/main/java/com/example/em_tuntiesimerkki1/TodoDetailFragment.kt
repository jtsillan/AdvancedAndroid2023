package com.example.em_tuntiesimerkki1

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
import com.example.em_tuntiesimerkki1.databinding.FragmentTodoDetailBinding
import com.example.em_tuntiesimerkki1.datatypes.comment.Comment
import com.example.em_tuntiesimerkki1.datatypes.todoitem.TodoItem
import com.google.gson.GsonBuilder


class TodoDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentTodoDetailBinding? = null

    private lateinit var adapter: TodoAdapter

    private val args: TodoDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getDetails()

        Log.d("TESTI", "id: " + args.id.toString())

        return root
    }

    private fun getDetails() {
        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos/${args.id}"
        // Alustetaan GSON
        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                //Log.d("ADVTECH", response)
                // Muutetaan raaka-JSON rajapinnasta (responce) GSON:n avulla listaksi Comment-objektiksi
                var item : TodoItem = gson.fromJson(response, TodoItem::class.java)

                Log.d("TESTI", "userId: " + item.userId.toString())
                Log.d("TESTI", "id: " + item.id.toString())
                Log.d("TESTI", "title: " + item.title)
                Log.d("TESTI", "completed: " + item.completed.toString())

                binding.textViewUserId.text = item.userId.toString()
                binding.textViewId.text = item.id.toString()
                binding.textViewTitle.text = item.title.toString()
                binding.textViewCompleted.text = item.completed.toString()

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
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