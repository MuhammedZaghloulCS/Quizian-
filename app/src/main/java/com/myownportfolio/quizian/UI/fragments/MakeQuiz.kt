package com.myownportfolio.quizian.UI.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
        binding = FragmentMakeQuizBinding.inflate(inflater, container, false)
        correctAnswer()
        binding.nextQuestion.setOnClickListener {
            nextQuestion()
        }

        binding.backButton.setOnClickListener {
            customDialog()
        }
        binding.finish.setOnClickListener {

            if (nextQuestion()) {
                findNavController().navigate(
                    R.id.action_makeQuiz_to_showQuiz, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.home2, false)
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .setPopEnterAnim(android.R.anim.slide_out_right) // Built-in slide in from right for FragmentA
                        .setPopExitAnim(android.R.anim.slide_in_left)
                        .build()

                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            customDialog()
        }
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun nextQuestion(): Boolean {
        var answersList = listOf(
            binding.answer1.text.toString(),
            binding.answer2.text.toString(),
            binding.answer3.text.toString(),
            binding.answer4.text.toString()
        )
        answersList = answersList.filter { it.isNotEmpty() }

        var push = true
        if (binding.question.text.isEmpty()) {
            binding.attention.visibility = View.VISIBLE
            push = false
        } else
            binding.attention.visibility = View.INVISIBLE
        if (answersList.size < 2) {
            binding.attention2.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.red
                )
            )
            push = false

        } else {
            binding.attention2.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )

        }
        if (isCorrectAnswerSelected() && !isTheCorrectAnswerEmpty()) {
            binding.attention3.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            )

        } else {
            if (isCorrectAnswerSelected())
                if (isTheCorrectAnswerEmpty())
                    binding.attention3.text = "Please select non empty answer"
            binding.attention3.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.red
                )
            )
            push = false
        }


        if (push) {
            currentQuestionIndex++
            val answersWithCorrectAnswer= listOf(getTheCorrectAnswer())+answersList
            answersWithCorrectAnswer.filter { it!="" }
            mainViewModel.addQuestion(binding.question.text.toString(), answersWithCorrectAnswer)
            binding.numOfQues.text =
                (currentQuestionIndex + 1).toString() + '/' + (mainViewModel.questionList.value.size).toString()
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
        val dialogView =
            dialog.findViewById<View>(R.id.custom_dialog) // Adjust ID to match your layout

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

    fun correctAnswer() {
        val answers = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
        answers.forEach {
            it.setOnLongClickListener {
                answers.apply {
                    forEach {
                        it.setBackgroundColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                android.R.color.transparent
                            )
                        )
                    }
                }
                it.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        android.R.color.holo_green_light
                    )
                )
                true
            }
        }
    }

    fun isCorrectAnswerSelected(): Boolean {
        val answers = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)

        answers.forEach {
            val backgroundColor = (it.background as? ColorDrawable)?.color
            val correctColor =
                ContextCompat.getColor(binding.root.context, android.R.color.holo_green_light)

            if (backgroundColor == correctColor) {
                return true // A correct answer is selected
            }
        }
        return false // No correct answer selected
    }

    fun isTheCorrectAnswerEmpty(): Boolean {
        val answersList = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
        Log.i("Main", answersList[3].text.isEmpty().toString())
        Log.i("Main", android.R.color.holo_green_light.toString())
        Log.i("Main", answersList[3].background.toString())
        answersList.forEach {

            if ((it.background as? ColorDrawable)?.color == ContextCompat.getColor(
                    binding.root.context,
                    android.R.color.holo_green_light
                )
            ) {
                return it.text.isEmpty()
            }
        }
        return true
    }
    fun getTheCorrectAnswer():String
    {
        val answersList = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
        answersList.forEach {
            if((it.background as? ColorDrawable)?.color==ContextCompat.getColor(binding.root.context,android.R.color.holo_green_light))
                return it.text.toString()
        }
        return ""
    }

}