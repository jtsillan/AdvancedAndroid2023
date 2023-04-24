package com.example.em_tuntiesimerkki1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.em_tuntiesimerkki1.databinding.FragmentCustomViewTestBinding
import java.util.UUID
import kotlin.random.Random


class CustomViewTestFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentCustomViewTestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomViewTestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // the binding -object allows you to access views in the layout, textviews etc.
        binding.customTemperature.changeTemperature((-30..-20).random())

        binding.buttonChangeTemperature.setOnClickListener {
            binding.customTemperature.changeTemperature((-30..30).random())
        }

        binding.buttonAddTestMessage.setOnClickListener {
            var text = UUID.randomUUID().toString()
            binding.latestDataTestView.addData(text)
        }
        // Setting animation_list.xml as the background of the image view
//        binding.imageViewRobot.setBackgroundResource(R.drawable.robot_animation)
//        val frameAnimation = binding.imageViewRobot.background as AnimationDrawable
//        frameAnimation.start()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}