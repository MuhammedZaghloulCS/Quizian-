package com.myownportfolio.quizian.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentHomeBinding

class Home : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.appName.animate().apply {
            duration=2000
            alpha(1f)
        }.start()
        binding.prepareQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_makeQuiz)
        }

        return binding.root
    }

}