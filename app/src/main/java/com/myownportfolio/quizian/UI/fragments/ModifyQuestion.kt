package com.myownportfolio.quizian.UI.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentModifyQuestionBinding

class ModifyQuestion : Fragment() {
    lateinit var binding: FragmentModifyQuestionBinding
    val safArgs: ModifyQuestionArgs by navArgs()
    val mainViewModel: QuestionViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModifyQuestionBinding.inflate(layoutInflater)
        val answersEditText = mutableListOf(
            binding.answer1Modify, binding.answer2Modify,
            binding.answer3Modify, binding.answer4Modify
        )
        val question: String = safArgs.question
//        Toast.makeText(binding.root.context,mainViewModel.returnValuesByKey(question).size.toString() , Toast.LENGTH_SHORT).show()
        binding.questionModify.setText(question)
        mainViewModel.returnValuesByKey(question).forEachIndexed { index, s ->
            answersEditText.get(index).setText(s)
        }
        binding.savechanges.setOnClickListener {
            confirmChanges(false)
        }
        binding.cancelModification.setOnClickListener {
            mainViewModel.isEqual(binding.questionModify.text.toString(),getAnswers()).apply {
                findNavController().popBackStack()
                return@setOnClickListener
            }
            confirmChanges(true)
        }


        return binding.root
    }

    private fun editQuestion() {
        var answersList = listOf(
            binding.answer1Modify.text.toString(),
            binding.answer2Modify.text.toString(),
            binding.answer3Modify.text.toString(),
            binding.answer4Modify.text.toString()
        )
        answersList = answersList.filter { it.isNotEmpty() }
        var push = true
        if (binding.questionModify.text.isEmpty()) {
            binding.attentionModify.visibility = View.VISIBLE
            push = false
        } else
            binding.attentionModify.visibility = View.INVISIBLE
        if (answersList.size < 2) {
            binding.attention2Modify.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.red
                )
            )
            push = false

        } else {
            binding.attention2Modify.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )

        }
        if (push) {

            mainViewModel.addQuestion(binding.questionModify.text.toString(), answersList)
            binding.questionModify.text.clear()
            binding.answer1Modify.text.clear()
            binding.answer2Modify.text.clear()
            binding.answer3Modify.text.clear()
            binding.answer4Modify.text.clear()
            findNavController().popBackStack()
        }
    }

    private fun confirmChanges(cancel:Boolean) {
        val dialog = Dialog(binding.root.context)
        dialog.setContentView(R.layout.custom_dialog)

        // Access the root view of the dialog
        val dialogView =
            dialog.findViewById<View>(R.id.custom_dialog) // Adjust ID to match your layout
        val title=dialog.findViewById<TextView>(R.id.dialog_title)


        val content=dialog.findViewById<TextView>(R.id.dialog_content)
        if(!cancel)
        {
            title.text="Save Changes"
            content.text="Are you sure you want to save the Quiz modification?"
        }
        else
        {
            title.text="Discard Modification"
            content.text="Are you sure you want to cancel the Quiz modification?"
        }
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
            editQuestion()
            findNavController().popBackStack()
        }
    }

    fun getAnswers(): List<String> {
        return listOf(
            binding.answer1Modify.text.toString(),
            binding.answer2Modify.text.toString(),
            binding.answer3Modify.text.toString(),
            binding.answer4Modify.text.toString()
        )
    }
}