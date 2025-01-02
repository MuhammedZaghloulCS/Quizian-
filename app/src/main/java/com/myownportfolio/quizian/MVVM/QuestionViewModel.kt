package com.myownportfolio.quizian.MVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestionViewModel(): ViewModel() {
    private val _questionList= MutableStateFlow<Map<String,List<String>>>(emptyMap())
    val questionList=_questionList.asStateFlow()
    fun addQuestion(question:String,answers:List<String>)
    {
        _questionList.value+=question to answers

    }
    fun returnKeys():List<String>
    {
        return _questionList.value.keys.toList()

    }
}