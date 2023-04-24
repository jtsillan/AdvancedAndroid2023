package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.em_tuntiesimerkki1.databinding.FragmentDataReadBinding

class DataReadFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentDataReadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonDataDetail.setOnClickListener {
            Log.d("TESTI", "NAPPI TOIMII")

            val action = DataReadFragmentDirections.actionDataReadFragmentToDataDetailFragment(69)
            it.findNavController().navigate(action)
        }

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}