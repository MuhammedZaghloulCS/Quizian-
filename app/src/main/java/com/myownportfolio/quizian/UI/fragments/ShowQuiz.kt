package com.myownportfolio.quizian.UI.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myownportfolio.quizian.Adapter.QuestionAdapter
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentShowQuizBinding
import com.myownportfolio.quizian.databinding.QuestionItemBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ShowQuiz : Fragment() {
    private lateinit var binding: FragmentShowQuizBinding
    val myViewModel: QuestionViewModel by activityViewModels()
    val database = Firebase.database
    val myRef = database.getReference("codes")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShowQuizBinding.inflate(inflater, container, false)
        val adapter = QuestionAdapter(mutableListOf(),
            onClickListener = { it ->
                val action = ShowQuizDirections.actionShowQuizToModifyQuestion(it)
                findNavController().navigate(action)
            },
            confirmDeleting = { it ->
                confirmDeleting(it)

            }
        )
        lifecycleScope.launch {
            myViewModel.questionList.collect { it ->
                val keys = it.map { it.question }
                if (keys.isEmpty())
                    findNavController().popBackStack()
            }

        }
        binding.questionRecycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            myViewModel.questionList.collect { updatedList ->
                adapter.run {
                    updateList(updatedList.map { it.question }.toMutableList())
                }

            }

        }

        binding.send.setOnClickListener {
            val code = generateUniqueCode()
            myRef.child(code)
                .setValue(myViewModel.questionList.value.associate { it.question to it.answers })
            val action = ShowQuizDirections.actionShowQuizToUniqueCode(code)
            findNavController().navigate(
                action,
                NavOptions.Builder().setPopUpTo(R.id.home2, false).build()
            )

        }


        return binding.root

    }

    fun generateUniqueCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    fun confirmDeleting(questionKey: String) {
        val dialog = Dialog(binding.root.context)
        dialog.setContentView(R.layout.custom_dialog)
        val dialogView = dialog.findViewById<ViewGroup>(R.id.custom_dialog)
        dialogView.alpha = 0f
        dialog.show()
        dialogView.animate().alpha(1f).setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator()).start()
        val title = dialogView.findViewById<TextView>(R.id.dialog_title)
        val content = dialogView.findViewById<TextView>(R.id.dialog_content)
        title.text = "Confirm Deleting"
        content.text = "Are you sure you want to delete this Question?"

        dialogView.findViewById<TextView>(R.id.cancel).apply {
            setOnClickListener {
                dialog.dismiss()
            }
        }
        dialogView.findViewById<TextView>(R.id.confirm).apply {
            setOnClickListener {
                dialog.dismiss()
                myViewModel.deleteQuestion(questionKey)

            }
        }
    }

    fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback {
            val dialog = Dialog(binding.root.context)
            dialog.setContentView(R.layout.custom_dialog)
            val dialogView = dialog.findViewById<ViewGroup>(R.id.custom_dialog)
            dialogView.alpha = 0f
            dialog.show()
            dialogView.animate().alpha(1f).setDuration(1500)
                .setInterpolator(AccelerateDecelerateInterpolator()).start()
            dialogView.findViewById<TextView>(R.id.dialog_title).text = "Confirm Leaving"
            dialogView.findViewById<TextView>(R.id.dialog_content).text =
                "Are you sure that you want to cancel the Quiz?"
            dialogView.findViewById<TextView>(R.id.cancel).apply {
                setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialogView.findViewById<TextView>(R.id.confirm).apply {
                setOnClickListener {
                    dialog.dismiss()
                    findNavController().popBackStack()
                    myViewModel.questionList.value.clear()
                }
            }
        }
    }

}