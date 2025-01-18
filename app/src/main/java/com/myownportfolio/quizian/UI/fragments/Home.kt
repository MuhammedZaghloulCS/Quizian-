package com.myownportfolio.quizian.UI.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val myViewModel: QuestionViewModel by activityViewModels()
    private val database = Firebase.database
    private val myRef = database.getReference("codes")
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel.questionList.value.clear()
        myViewModel.questionListToDownLoad.value.clear()
        myViewModel.destoryPointer()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        setupObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPress()
        }

        return binding.root
    }

    private fun setupUI() {
        binding.appName.animate().apply {
            duration = 2000
            alpha(1f)
        }.start()

        binding.search.setOnClickListener {
            if (isInternetAvailable(binding.root.context)) {
                val code = binding.serchCode.text.toString()
                progressDialog()
                if (code.isNotBlank()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        search(code)
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter a code", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No internet connection. Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.prepareQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_makeQuiz)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            myViewModel.questionListToDownLoad.collect { questions ->
                if (questions.isNotEmpty()) {
                    // Update UI with the questions (e.g., show in a RecyclerView or log them)
                    progressDialogDismiss()
                    findNavController().navigate(R.id.quiz)
                } else {
                    Toast.makeText(requireContext(), "No questions found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun onBackPress() {
        requireActivity().finish()
    }

    private fun search(code: String) {
        try {
            myViewModel.getQuestionsWithAnswers(code)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to load questions, please try again later",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun progressDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progressbar)
        val dialogView = dialog.findViewById<View>(R.id.progress_circular)
        dialogView.visibility = View.VISIBLE
        dialog.show()

    }

    private fun progressDialogDismiss() {
        dialog.dismiss()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }
}






