package com.myownportfolio.quizian.UI.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.google.android.material.button.MaterialButton
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentQuizBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Quiz : Fragment() {
    lateinit var binding: FragmentQuizBinding
    lateinit var buttons: List<MaterialButton>
    val mainViewModel: QuestionViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        buttons = listOf(
            binding.quizAnswer1,
            binding.quizAnswer2,
            binding.quizAnswer3,
            binding.quizAnswer4
        )

        binding.nextQuestion.setOnClickListener {
            listOf(
                binding.root.findViewById<MaterialButton>(R.id.quiz_answer3),
                binding.root.findViewById<MaterialButton>(R.id.quiz_answer4)
            ).forEach {
                it.visibility = View.GONE
                it.text = ""
            }
            mainViewModel.nextQuestion()
        }

        lifecycleScope.launch {
            mainViewModel.quizPointer.collect { it ->
                if (it+1 == mainViewModel.questionListToDownLoad.value.size) {
                    binding.nextQuestion.text = "<< Finish"
                }
                if (it != -1) {
                    binding.quizQuestion.text =
                        mainViewModel.questionListToDownLoad.value[it].question
                    binding.quizAnswer1.text =
                        mainViewModel.questionListToDownLoad.value[it].answers[0]
                    val hiddenButtons = listOf(
                        binding.root.findViewById<MaterialButton>(R.id.quiz_answer3),
                        binding.root.findViewById<MaterialButton>(R.id.quiz_answer4)
                    )
                    for (i in 0 until mainViewModel.questionListToDownLoad.value[it].answers.size - 2) {
                        withContext(Dispatchers.Main)
                        {
                            hiddenButtons[i].text =
                                mainViewModel.questionListToDownLoad.value[it].answers[i + 2]
                            hiddenButtons[i].visibility = View.VISIBLE
                        }


                    }
                    Toast.makeText(binding.root.context, mainViewModel.quizPointer.value.toString(), Toast.LENGTH_SHORT).show()

                }
                else
                {
                    findNavController().navigate(R.id.results,NavOptions.Builder().setPopUpTo(R.id.home2,false).build())
                }

            }

        }

        buttons.forEach { it ->
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

    override fun onDestroy() {
        mainViewModel.destoryPointer()
        super.onDestroy()
    }

}