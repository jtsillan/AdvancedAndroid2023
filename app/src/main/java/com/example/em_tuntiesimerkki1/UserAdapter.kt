package com.example.em_tuntiesimerkki1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.em_tuntiesimerkki1.databinding.RecyclerviewUserRowBinding

class UserAdapter(private val users : List<User>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    private var _binding: RecyclerviewUserRowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        _binding = RecyclerviewUserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val itemUser = users[position]
        holder.bindUser(itemUser)
    }

    class UserHolder(v: RecyclerviewUserRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        private var view: RecyclerviewUserRowBinding = v
        private var user: User? = null

        init {
            v.root.setOnClickListener(this)
        }

        fun bindUser(user: User) {
            this.user = user

            var userName : String = user.name as String
            var userEmail : String = user.email as String

            view.textViewUser.text = userName
            view.textViewEmail.text = userEmail
        }

        override fun onClick(v: View?) {
            Log.d("ADVTECH", "RecyclerView clicked!!!" + user?.userId.toString())
        }

    }

}