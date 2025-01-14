package com.myownportfolio.quizian.Adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.QuestionItemBinding

class QuestionAdapter(
    private var question: MutableList<String>,
    val onClickListener: (q: String) -> Unit,
    val confirmDeleting: (key:String)->Unit
) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String) {
            binding.questionPlaceHolder.text = question
            binding.root.setOnClickListener {
                onClickListener(question)
            }
            binding.deleteQuestion.setOnClickListener {
                confirmDeleting(question)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            QuestionItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return question.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(question[position])


    }


    fun updateList(newItems: List<String>) {
        question.clear()
        question.addAll(newItems)
        notifyDataSetChanged()
    }


}