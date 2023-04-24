package com.example.em_tuntiesimerkki1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.em_tuntiesimerkki1.databinding.RecyclerviewItemRowBinding
import com.example.em_tuntiesimerkki1.datatypes.comment.Comment

class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    // Initialize variables for binding layer
    private var _binding: RecyclerviewItemRowBinding? = null
    private val binding get() = _binding!!

    // ViewHolder onCreate method, binds binding layer part of CommentHolder -class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        // Single recyclerview_item_row.xml instance works as a binging layer
        _binding = RecyclerviewItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    // Binds single Comment-object to CommentHolder instance
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val itemComment = comments[position]
        holder.bindComment(itemComment)
    }

    // Adapter most know its size, mandatory class item
    override fun getItemCount(): Int {
        return comments.size
    }

    // Defines logic which binds the view and data together
    class CommentHolder(v: RecyclerviewItemRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        // Variables for view and data
        private var view: RecyclerviewItemRowBinding = v
        private var comment: Comment? = null

        // Enables single item click in this class
        init {
            v.root.setOnClickListener(this)
        }

        // Method that binds data details to view details
        fun bindComment(comment: Comment) {
            // Define comment for this method
            this.comment = comment

            // Save comment name as string
            var commentName : String = comment.name as String

            // If comment name is longer that 20 signs, shorten and add "..."
            if(commentName.length > 20) {
                commentName = commentName.substring(0, 20) + "..."
            }

            // Set comment name to text view
            view.textViewCommentName.text = commentName

            // Set other details to ui
            view.textViewCommentEmail.text = comment.email
            view.textViewCommentBody.text = comment.body
        }

        // Method to run when single short click is made
        override fun onClick(v: View) {
            Log.d("ADVTECH", "RecyclerView clicked!!!" + comment?.id.toString())

            // Cast comment id to Int
            val commentId = comment?.id as Int

            // Navigate to ApiDetailFragment, comment id as parameter
            val action = CommentApiFragmentDirections.actionCommentApiFragmentToApiDetailFragment(commentId)
            v.findNavController().navigate(action)
        }
    }
}