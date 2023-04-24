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

    // Initialize variables for binding layer
    private var _binding: RecyclerviewTodoRowBinding? = null
    private val binding get() = _binding!!

    // ViewHolder onCreate method, binds binding layer part of TodoHolder -class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        // Single recyclerview_todo_row.xml instance works as a binging layer
        _binding = RecyclerviewTodoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoHolder(binding)
    }

    // Binds single Todoo-object to TodoHolder instance
    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val itemTodo = todos[position]
        holder.bindTodo(itemTodo)
    }

    // Adapter most know its size, mandatory class item
    override fun getItemCount(): Int {
        return todos.size
    }

    // Defines logic which binds the view and data together
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
            else {
                // RecyclerView needs else-statement, otherwise sign is not shown
                view.imageViewCompleted.setImageResource(R.drawable.check_box_ok)
            }

            // Set todoTitle to view as text
            view.textViewTodoTitle.text = todoTitle
        }

        // When one item is clicked this code is run
        override fun onClick(v: View) {

            Log.d("ADVTECH", "RecyclerView clicked!!!" + todo?.id.toString())

            // Set todoItem values to variables and pass to action
            val todoId = todo?.id as Int

            // Navigate to TodoDetailFragment, item id as parameter
            val action = TodoItemFragmentDirections.actionTodoFragmentToTodoDetailFragment(todoId)
            v.findNavController().navigate(action)
        }

    }
}