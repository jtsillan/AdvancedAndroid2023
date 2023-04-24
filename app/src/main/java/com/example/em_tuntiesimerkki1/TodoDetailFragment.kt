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
import com.example.em_tuntiesimerkki1.datatypes.todouseritem.TodoUserItem
import com.google.gson.GsonBuilder


class TodoDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentTodoDetailBinding? = null

    private lateinit var adapter: TodoAdapter

    private val args: TodoDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private var userId : String = ""
    private var id : String = ""
    private var title : String = ""
    private var completed : String = ""
    private var name : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Function call to access url
        getDetails()

        Log.d("TESTI", "id: " + args.id.toString())

        return root
    }

    private fun getDetails() {
        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos/${args.id}"
        // Initialize GSON
        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                // Change raw-JSON data from interface (response) to list of TodoItem objects with GSON
                var item : TodoItem = gson.fromJson(response, TodoItem::class.java)

                // Set variables to class members to use it later on
                id = item.id.toString()
                title = item.title.toString()
                completed = item.completed.toString()

                // Chain request to show in UI simultaneous
                getUserDetails()
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

    private fun getUserDetails() {

        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/users/${args.id}"

        // Initialize GSON
        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                // Change raw-JSON data from interface (response) to list of TodoUserItem objects with GSON
                var userItem : TodoUserItem = gson.fromJson(response, TodoUserItem::class.java)

                // Bing the views and from class level
                binding.textViewUserName.text = userItem.name.toString()
                binding.textViewId.text = id
                binding.textViewTitle.text = title
                binding.textViewCompleted.text = completed

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