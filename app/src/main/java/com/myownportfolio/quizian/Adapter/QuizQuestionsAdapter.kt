package com.myownportfolio.quizian.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myownportfolio.quizian.databinding.QuestionItemBinding

class QuestionAdapter(private var question: List<String> ) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {
   inner class MyViewHolder(private val binding:QuestionItemBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(question: String)
        {
            binding.questionPlaceHolder.text=question
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(QuestionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount():Int{
        return question.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(question[position])
        Log.i("Main", "Binding question: ${question[position]}")
    }
}