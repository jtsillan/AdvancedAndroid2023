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

    // binding layerin muuttujien alustaminen
    private var _binding: RecyclerviewItemRowBinding? = null
    private val binding get() = _binding!!

    // ViewHolderin onCreate-metodi. käytännössä tässä kytketään binding layer
    // osaksi CommentHolder-luokkaan (adapterin sisäinen luokka)
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        // binding layerina toimii yksitätinen recyclerview_item_row.xml -instanssi
        _binding = RecyclerviewItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    // tämä metodi kytkee yksittäisen Comment-objektin yksittäisen CommentHolder-instanssiin
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onBindViewHolder
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val itemComment = comments[position]
        holder.bindComment(itemComment)
    }

    // Adapterin täytyy pysty tietämään sisältämänsä datan koko tämän metodin avulla
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä getItemCount
    override fun getItemCount(): Int {
        return comments.size
    }

    // CommentHolder, joka määritettiin oman CommentAdapterin perusmäärityksessä (ks. luokan yläosa)
    // Holder-luokka sisältää logiikan, jolla data ja ulkoasu kytketään toisiinsa
    class CommentHolder(v: RecyclerviewItemRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        // tämän kommentin ulkoasu ja varsinainen data
        private var view: RecyclerviewItemRowBinding = v
        private var comment: Comment? = null

        // mahdollistetaan yksittäisen itemin klikkaaminen tässä luokassa
        init {
            v.root.setOnClickListener(this)
        }

        // metodi, joka kytkee datan yksityiskohdat ulkoasun yksityiskohtiin
        fun bindComment(comment: Comment) {
            // otetaan kommentti talteen ylätasolla, että voimme käyttää sitä myös esim. onClick
            this.comment = comment

            // Tallennetaan kommentin nimi Stringiksi
            var commentName : String = comment.name as String

            // Jos kemmentin pituus on yli 20 merkkiä, lyhennetään 20:een merkkiin
            // ja lisätään kolme pistettä
            if(commentName.length > 20) {
                commentName = commentName.substring(0, 20) + "..."
            }

            // oli kommentin nimi mitä tahansa, asetetaan se käyttöliittymään
            view.textViewCommentName.text = commentName

            // asetetaan muut tiedot ulkoasuun
            view.textViewCommentEmail.text = comment.email
            view.textViewCommentBody.text = comment.body
        }

        // jos itemiä klikataan käyttöliittymässä, ajetaan tämä koodi
        override fun onClick(v: View) {
            Log.d("ADVTECH", "RecyclerView clicked!!!" + comment?.id.toString())

            // muutetaan Int? => Int
            val commentId = comment?.id as Int

            // navigoidaan ApiFragmentista --> ApiDetailFragment, parametrina kommentin id
            val action = CommentApiFragmentDirections.actionCommentApiFragmentToApiDetailFragment(commentId)
            v.findNavController().navigate(action)
        }
    }
}