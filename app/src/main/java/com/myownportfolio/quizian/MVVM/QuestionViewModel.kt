package com.myownportfolio.quizian.MVVM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myownportfolio.quizian.pojo.QuestionWithAnswers
import com.myownportfolio.quizian.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionViewModel : ViewModel() {
    private val repo = Repository()

    // StateFlow to hold the list of questions with their answers
    private val _questionList = MutableStateFlow<MutableList<QuestionWithAnswers>>(mutableListOf())
    val questionList = _questionList.asStateFlow()
    // StateFlow to track the loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    // StateFlow to hold the list of questions to download
    private val _questionListToDownLoad = MutableStateFlow<MutableList<QuestionWithAnswers>>(mutableListOf())
    val questionListToDownLoad = _questionListToDownLoad.asStateFlow()
    // StateFlow to track the current quiz pointer
    private val _quizPointer= MutableStateFlow(0)
    val quizPointer=_quizPointer.asStateFlow()



    // Adds a new question with its answers to the list
    fun addQuestion(question: String, answers: List<String>) {
        _questionList.update { (it + QuestionWithAnswers(question, answers)).toMutableList() }
    }

    // Returns all question keys
    fun returnKeys(): List<String> {
        return _questionList.value.map { it.question }
    }

    // Returns the answers associated with a specific question key
    fun returnValuesByKey(keyQuestion: String): List<String> {
        return _questionList.value.find { it.question == keyQuestion }?.answers ?: emptyList()
    }

    // Deletes a question by its key
    fun deleteQuestion(q: String) {
        _questionList.update { it.filterNot { question -> question.question == q }.toMutableList() }
    }

    // Checks if a question with a specific key and answers exists in the list
    fun isEqual(key: String, answers: List<String>): Boolean {
        return _questionList.value.any { it.question == key && it.answers == answers }
    }
    fun returnKeysQuiz(): List<String> {
        return _questionListToDownLoad.value.map { it.question }
    }

    // Returns the answers associated with a specific question key
    fun returnValuesByKeyQuiz(keyQuestion: String): List<String> {
        return _questionListToDownLoad.value.find { it.question == keyQuestion }?.answers ?: emptyList()
    }

    // Checks if the code exists in the Firebase database
    suspend fun codesContain(code: String): Boolean {
        return repo.codesContain(code)
    }

    // Fetches questions and their answers associated with a specific code
    fun getQuestionsWithAnswers(code: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val questions = repo.getQuestionsWithAnswers(code)
                _questionListToDownLoad.emit(questions)
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error fetching questions: ${e.message}")
            }
        }
    }

    fun nextQuestion() {
        if (quizPointer.value < questionListToDownLoad.value.size-1)
        _quizPointer.update { it + 1 }
        else
            _quizPointer.update { - 1 }

    }
    fun destoryPointer() {
        _quizPointer.update { 0 }
    }

    // Fetches answers for a specific question by its key
    fun getAnswersByQuestion(question: String, code: String) {
        viewModelScope.launch {
            try {
                getQuestionsWithAnswers(code)
                val answers = _questionListToDownLoad.value.find { it.question == question }?.answers ?: emptyList()
                // Do something with the answers if required
                Log.d("QuestionViewModel", "Answers for $question: $answers")
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error fetching answers: ${e.message}")
            }
        }
    }
}
