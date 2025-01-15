package com.myownportfolio.quizian.pojo

data class QuestionWithAnswers(var question: String, val answers: List<String>)
{
    fun correctAnswer():String{
        return answers.filter { it.count()>1 }[0]
    }
}
