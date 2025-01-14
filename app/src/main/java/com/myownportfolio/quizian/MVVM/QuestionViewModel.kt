package com.myownportfolio.quizian.MVVM

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestionViewModel(): ViewModel() {
    private val _questionList= MutableStateFlow<MutableMap<String,List<String>>>(mutableMapOf())
    val questionList=_questionList.asStateFlow()
    fun addQuestion(question:String,answers:List<String>)
    {
        _questionList.value+=question to answers

    }
    fun returnKeys():List<String>
    {
        return _questionList.value.keys.toList()

    }
    fun returnValuesByKey(keyQuestion:String):List<String>
    {
        return _questionList.value[keyQuestion]!!   }

    fun deleteQuestion(q:String)
    {
        val updatedMap = _questionList.value.toMutableMap() // Create a mutable copy
        updatedMap.remove(q) // Remove the key
        _questionList.value = updatedMap

    }
    fun isEqual(key:String,answers: List<String>) :Boolean
    {
        if(_questionList.value[key]==null)
            return false
        if (_questionList.value[key]!=null)
        {
            return _questionList.value[key]!!.toList()==answers
        }
        return false
    }
}