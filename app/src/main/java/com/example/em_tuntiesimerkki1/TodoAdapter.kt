package com.example.em_tuntiesimerkki1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.em_tuntiesimerkki1.databinding.RecyclerviewTodoRowBinding
import com.example.em_tuntiesimerkki1.datatypes.todoitem.TodoItem

class TodoAdapter(private val todos: List<TodoItem>) : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    // binding layerin muuttujien alustaminen
    private var _binding: RecyclerviewTodoRowBinding? = null
    private val binding get() = _binding!!

    // TodoHolderin onCreate-metodi. käytännössä tässä kytketään binding layer
    // osaksi TodoHolder-luokkaan (adapterin sisäinen luokka)
    // koska TodoAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        // binding layerina toimii yksitätinen recyclerview_item_row.xml -instanssi
        _binding = RecyclerviewTodoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoHolder(binding)
    }

    // tämä metodi kytkee yksittäisen toodo-objektin yksittäisen CommentHolder-instanssiin
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onBindViewHolder
    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val itemTodo = todos[position]
        holder.bindTodo(itemTodo)
    }

    // Adapterin täytyy pysty tietämään sisältämänsä datan koko tämän metodin avulla
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä getItemCount
    override fun getItemCount(): Int {
        return todos.size
    }

    class TodoHolder(v: RecyclerviewTodoRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        // Variables for storing TodoItem data and view
        private var view: RecyclerviewTodoRowBinding = v
        private var todo: TodoItem? = null

        // Enables to click single item in this class
        init {
            v.root.setOnClickListener(this)
        }

        // Method that binds data details to view details
        fun bindTodo(todo : TodoItem)
        {
            // Take single toodo-item, so it can be used later on
            this.todo = todo

            // Save todoTitle as string
            var todoTitle : String = todo.title as String

            // If todoTitle is longer than 25 signs, shorten and add '...'
            if (todoTitle.length > 25) {
                todoTitle = todoTitle.substring(0, 25) + "..."
            }

            // Set variable to store completed value as boolean
            var todoCompleted : Boolean = todo.completed as Boolean

            // Compare true/false value and change todoCompleted sign in view
            if (!todoCompleted) {
                // Set image as not checked
                view.imageViewCompleted.setImageResource(R.drawable.red_cross)
            }

            // Set todoTitle to view as text
            view.textViewTodoTitle.text = todoTitle
        }

        // When one item is clicked this code is run
        override fun onClick(v: View) {

            Log.d("ADVTECH", "RecyclerView clicked!!!" + todo?.id.toString())

            // Set todoItem values to variables and pass to action
            val todoId = todo?.id as Int
//            val todoUserId = todo?.userId as Int
//            val todoTitle = todo?.title as String
//            val todoCompleted = todo?.completed as Boolean

            // Navigate to TodoDetailFragment, item id as parameter
            val action = TodoItemFragmentDirections.actionTodoFragmentToTodoDetailFragment(todoId)
            v.findNavController().navigate(action)
        }

    }
}