package com.myownportfolio.quizian.UI.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentMakeQuizBinding


class MakeQuiz : Fragment() {
    private lateinit var binding: FragmentMakeQuizBinding
    val mainViewModel: QuestionViewModel by activityViewModels()
    private var currentQuestionIndex = -1 // To track the current question
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.questionList.value.clear()
    }
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

            if (nextQuestion())
            {
                findNavController().navigate(
                    R.id.action_makeQuiz_to_showQuiz, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.home2, false)
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .setPopEnterAnim(android.R.anim.slide_out_right) // Built-in slide in from right for FragmentA
                        .setPopExitAnim(android.R.anim.slide_in_left)
                        .build()

                )}
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            customDialog()
        }
        return binding.root
    }
    private fun nextQuestion():Boolean {
        var answersList=listOf(binding.answer1.text.toString(),
            binding.answer2.text.toString(),
            binding.answer3.text.toString(),
            binding.answer4.text.toString())
        answersList=answersList.filter { it.isNotEmpty() }
        var push=true
        if (binding.question.text.isEmpty())
        {
            binding.attention.visibility=View.VISIBLE
            push=false
        }
        else
            binding.attention.visibility=View.INVISIBLE
        if (answersList.size<2) {
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
            mainViewModel.addQuestion(binding.question.text.toString(),answersList)
            binding.numOfQues.text=(currentQuestionIndex+1).toString()+'/'+(mainViewModel.questionList.value.size).toString()
            binding.question.text.clear()
            binding.answer1.text.clear()
            binding.answer2.text.clear()
            binding.answer3.text.clear()
            binding.answer4.text.clear()
        }
        return push
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
            dialog.dismiss()
            findNavController().popBackStack()
            mainViewModel.questionList.value.clear()
        }
    }


}