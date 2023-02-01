package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.em_tuntiesimerkki1.databinding.FragmentDataDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DataDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentDataDetailBinding? = null

    val args: DataDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sending info to LogCat to see val action id
        Log.d("TESTI", "id: " + args.id.toString())

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
