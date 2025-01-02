package com.myownportfolio.quizian.UI.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentMakeQuizBinding
import kotlinx.coroutines.launch


class MakeQuiz : Fragment() {
    private lateinit var binding: FragmentMakeQuizBinding
    private val questions: MutableMap<String,List<String>> = mutableMapOf()
    private var currentQuestionIndex = -1 // To track the current question
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentMakeQuizBinding.inflate(inflater,container,false)

        binding.nextQuestion.setOnClickListener{
            nextQuestion()
        }

        binding.backButton.setOnClickListener{
            customDialog()
        }
        binding.finish.setOnClickListener{
            nextQuestion()
            findNavController().navigate(R.id.action_makeQuiz_to_showQuiz)
        }

        return binding.root
    }
    private fun nextQuestion() {
        var answersList=listOf(binding.answer1.text.toString(),
            binding.answer2.text.toString(),
            binding.answer3.text.toString(),
            binding.answer4.text.toString())
        answersList=answersList.filter { it.isEmpty() }
        var push=true
        if (binding.question.text.isEmpty())
        {
            binding.attention.visibility=View.VISIBLE
            push=false
        }
        else
            binding.attention.visibility=View.INVISIBLE
        if (answersList.size>2) {
            binding.attention2.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.red))
            push=false

        }

        else
        {
            binding.attention2.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )

        }
        if (push)
        {
            currentQuestionIndex++
            binding.numOfQues.text=(currentQuestionIndex+1).toString()+'/'+(questions.size+1).toString()
            questions[binding.question.text.toString()]=answersList
            Toast.makeText(activity?.applicationContext, questions.keys.toString(), Toast.LENGTH_LONG).show()
            binding.question.text.clear()
            binding.answer1.text.clear()
            binding.answer2.text.clear()
            binding.answer3.text.clear()
            binding.answer4.text.clear()
        }
    }

    private fun customDialog() {
        val dialog = Dialog(binding.root.context)
        dialog.setContentView(R.layout.custom_dialog)

        // Access the root view of the dialog
        val dialogView = dialog.findViewById<View>(R.id.custom_dialog) // Adjust ID to match your layout

        // Set initial alpha to 0
        dialogView.alpha = 0f

        // Show the dialog first
        dialog.show()

        // Animate the alpha to create a fade-in effect
        dialogView.animate()
            .alpha(1f)
            .setDuration(1500) // Animation duration: 500ms
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .start()

        // Set close button functionality
        val closeButton = dialog.findViewById<Button>(R.id.cancel)
        closeButton?.setOnClickListener {
            dialog.dismiss()
        }
        val confirmButton = dialog.findViewById<Button>(R.id.confirm)
        confirmButton?.setOnClickListener {
            Toast.makeText(binding.root.context,"TODO",Toast.LENGTH_LONG).show()
        }
    }


}