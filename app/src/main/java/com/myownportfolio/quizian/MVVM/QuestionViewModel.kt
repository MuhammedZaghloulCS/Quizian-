package com.myownportfolio.quizian.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myownportfolio.quizian.pojo.QuestionWithAnswers
import com.myownportfolio.quizian.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionViewModel() : ViewModel() {
    private val repo = Repository()
    private val _questionList = MutableStateFlow<MutableList<QuestionWithAnswers>>(mutableListOf())
    val questionList = _questionList.asStateFlow()
    fun addQuestion(question: String, answers: List<String>) {
        _questionList.value += QuestionWithAnswers(question, answers)


    }

    fun returnKeys(): List<String> {
        return _questionList.value.map { it.question }

    }

    fun returnValuesByKey(keyQuestion: String): List<String> {
        return _questionList.value.find { it.question == keyQuestion }?.answers ?: emptyList()
    }

    fun deleteQuestion(q: String) {
        val updatedMap = _questionList.value.toMutableList()  // Create a mutable copy
        updatedMap.find { it.question == q }?.let { updatedMap.remove(it) } // Remove the key
        _questionList.value = updatedMap

    }

    fun isEqual(key: String, answers: List<String>): Boolean {
        return if (_questionList.value.find { it.question == key } == null)
            false
        else {
            _questionList.value.find { it.question == key }!!.answers == answers
        }
    }

    suspend fun getCodes(): Job {
        return viewModelScope.launch {
            repo.getCodes()
        }
    }

    suspend fun codesContain(code: String): Job {
        return viewModelScope.launch {
            repo.codesContain(code)
        }
    }

    suspend fun getQuestionsWithAnswers(code: String) {
        viewModelScope.launch {
            _questionList.value = repo.getQuestionsWithAnswers(code)

        }
    }

    suspend fun getAnswersByQuestion(
        question: String,
        code: String,
    ) {
        viewModelScope.launch {
            getQuestionsWithAnswers(code)
            _questionList.value.find { it.question == question }?.answers ?: emptyList()
        }
    }
}
