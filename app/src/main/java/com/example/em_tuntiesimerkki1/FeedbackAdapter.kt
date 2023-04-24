package com.example.em_tuntiesimerkki1

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.RecyclerviewFeedbackRowBinding
import com.example.em_tuntiesimerkki1.datatypes.feedback.Feedback
import org.json.JSONObject

class FeedbackAdapter(private val feedbacks: MutableList<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder>() {

    // Initialize variables for binding layer
    private var _binding: RecyclerviewFeedbackRowBinding? = null
    private val binding get() = _binding!!

    // ViewHolder onCreate method, binds binding layer part of FeedbackHolder -class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackHolder {
        // Single recyclerview_item_row.xml instance works as a binging layer
        _binding = RecyclerviewFeedbackRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackHolder(binding)
    }

    // Binds single Comment-object to FeedbackHolder instance
    override fun onBindViewHolder(holder: FeedbackHolder, position: Int) {
        val itemFeedback = feedbacks[position]
        holder.bindFeedback(itemFeedback)
    }

    // Adapter most know its size, mandatory class item
    override fun getItemCount(): Int {
        return feedbacks.size
    }

    // Method for removing single feedback item
    fun removeFeedback(position: Int) {
        feedbacks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, feedbacks.size)
    }


    // Defines logic which binds the view and data together
    inner class FeedbackHolder(v: RecyclerviewFeedbackRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener, View.OnLongClickListener {

        // Variables for view and data
        private var view: RecyclerviewFeedbackRowBinding = v
        private var feedback: Feedback? = null

        // Enables single item click in this class
        init {
            // For single click
            v.root.setOnLongClickListener(this)
            // For single long click
            v.root.setOnClickListener(this)
        }

        // Method that binds data details to view details
        fun bindFeedback(feedback: Feedback) {
            // Define comment for this method
            this.feedback = feedback

            // Save feeback name as string
            var feedbackName : String = feedback.name as String

            // Set feedback values to ui
            view.textViewFeedbackName.text = feedbackName
            view.textViewFeedbackValue.text = feedback.value

        }

        // Method to run when single short click is made
        override fun onClick(v: View) {
            Log.d("ADVTECH", "RecyclerView clicked!!!" + feedback?.id.toString())

        }

        // Method to run when single long click is made
        override fun onLongClick(v: View?): Boolean {
            Log.d("ADVTECH", "LONG CLICK!!!" + feedback?.id.toString())

            // Create alert dialog
            val adb = AlertDialog.Builder(view.root.context)

            // Define alert box action for "OK" click
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(view.root.context, "Feedback removed", Toast.LENGTH_LONG).show()
                deleteFeedback()
                removeFeedback(this.adapterPosition)
            }

            // Define alert box action for "CANCEL" click
            val negativeButtonClick = { dialog : DialogInterface, which : Int ->
                Toast.makeText(view.root.context, "Action canceled", Toast.LENGTH_LONG).show()
            }

            // Set alert box
            with(adb) {
                setTitle("Do you want to delete feedback?")
                setIcon(R.drawable.alert_icon)
                setPositiveButton("OK", DialogInterface.OnClickListener(positiveButtonClick))
                setNegativeButton("CANCEL", negativeButtonClick)
                show()
            }
            return true
        }

        // Method for delete single feedback
        private fun deleteFeedback() {
            val JSON_URL = "${BuildConfig.JSON_URL_DIRECTUS}/${feedback?.id}?access_token=${BuildConfig.DIRECTUS_ACCESS_TOKEN}"
            // Request a string response from the provided URL.
            val stringRequest: StringRequest = object : StringRequest(
                Method.DELETE, JSON_URL,
                Response.Listener { response ->
                    Log.d("ADVTECH", "Feedback removed!")

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
            val requestQueue = Volley.newRequestQueue(view.root.context)
            requestQueue.add(stringRequest)
        }
    }
}


