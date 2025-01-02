package com.myownportfolio.quizian.UI.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myownportfolio.quizian.Adapter.QuestionAdapter
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.databinding.FragmentShowQuizBinding
import com.myownportfolio.quizian.databinding.QuestionItemBinding


class ShowQuiz : Fragment() {
    private lateinit var binding: FragmentShowQuizBinding
    val myViewModel: QuestionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShowQuizBinding.inflate(inflater, container, false)
        binding.questionRecycler.apply {
            val answers = myViewModel.returnKeys()
            Log.d("Main", "Questions: $answers")
            adapter =
                QuestionAdapter(listOf("sfsfgsfgsfg", "sfsfgsfgsfg", "sfsfgsfgsfg", "sfsfgsfgsfg"))


            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }


        return binding.root

    }
}