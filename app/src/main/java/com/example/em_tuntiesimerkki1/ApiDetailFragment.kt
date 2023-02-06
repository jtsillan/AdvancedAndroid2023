package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.em_tuntiesimerkki1.databinding.FragmentApiDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ApiDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApiDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentApiDetailBinding? = null

    val args: ApiDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sending info to LogCat to see val action id
        Log.d("TESTI", "id: " + args.id.toString())

        // TODO, get data to individual comment
        // Tästä eteenpäin data voi hakea kolmella tavalla:

        // 1. Käytä id:tä Url:n muodostamisessa(ks. ylempää JSON_URL) ja ha Volleylla sen avulla
        // pelkästään kyseisen id:n takana oleva kommenttidata (yksittäinen JSON-objekti)
        // huomaa tässä tapauksessa, että GSON -muunnoksessa pitää käyttää yhden objektin esimerkikiä
        // var item : Comment = gson.fromJson(response, Comment::class.java)

        // hyvät puolet: aina ajantasainen data
        // huonot puolet: enemmän kodia

        // 2. laita fragmenttien välille lisää parametreja (esim. kommentin id, name, email, body jne)
        // ja aseta nämä argumentit suoraan omiin TextVieweihin ApiDetailFragmentin koodissa

        // hyvät puolet: yksinkertainen koodi, huomattavasti vähemmän tietoliikennettä
        // huono puoli: data voi olla vanhentunutta

        // 3. muuta koko comment-olio aiemmassa fragmentissa JSON-formaattiin, ja siirrä vain
        // JSON-muuttuja tähän fragmenttiin

        // hyvät ja huonot puolet samat kuin vahtoehdossa 2, mutta lisäksi vaatii GSON-muunnoksen
        // molemmissa fragmenteissa
        // ApiFragment -> comment-olio -> JSON .... ja myöhemmin ApiDetailFragment: JSON -> comment-olio

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}