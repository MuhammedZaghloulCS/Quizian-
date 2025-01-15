package com.myownportfolio.quizian.UI.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentQuizBinding

class Quiz : Fragment() {
    lateinit var binding:FragmentQuizBinding
    lateinit var buttons:List<MaterialButton>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding=FragmentQuizBinding.inflate(layoutInflater,container,false)
        buttons= listOf(binding.quizAnswer1,binding.quizAnswer2,binding.quizAnswer3,binding.quizAnswer4)

        buttons.forEach { it->
            it.setOnClickListener {
                // Deselect all buttons
                buttons.forEach { it.isSelected = false }

                // Select the clicked button
                it.isSelected = true

                // Update the background or appearance based on the state
                buttons.forEach { it.setBackgroundColor(if (it.isSelected) Color.GREEN else Color.GRAY) }
            }
        }


        return binding.root
    }

}