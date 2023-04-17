package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.FragmentTodoItemBinding
import com.example.em_tuntiesimerkki1.datatypes.comment.Comment
import com.example.em_tuntiesimerkki1.datatypes.todoitem.TodoItem
import com.google.gson.GsonBuilder

class TodoItemFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentTodoItemBinding? = null

    // alustetaan viittaus adapteriin sekä luodaan LinearLayoutManager
    // RecyclerView tarvitsee jonkin LayoutManagerin, joista yksinkertaisin on Linear
    private lateinit var adapter: TodoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Luodaan layout manager ja kytketään se ulkoasun recyclerview
        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager

        // the binding -object allows you to access views in the layout, textviews etc.

        binding.buttonGetData.setOnClickListener {
            getTodoComments()
        }

        return root
    }

    private fun getTodoComments() {
        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos"
        // Alustetaan GSON
        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                //Log.d("ADVTECH", response)
                // Muutetaan raaka-JSON rajapinnasta (responce) GSON:n avulla listaksi Comment-objektiksi
                var rows : List<TodoItem> = gson.fromJson(response, Array<TodoItem>::class.java).toList()

                // if the fetched data (Valley/GSON) is in varible "rows", we can set the data like this:
                adapter = TodoAdapter(rows)
                binding.recyclerView.adapter = adapter

                // Print every todoItem title
                for (item in rows) {
                    Log.d("ADVTECT", "Title: " + item.title.toString())
                }

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