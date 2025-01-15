package com.myownportfolio.quizian.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myownportfolio.quizian.pojo.QuestionWithAnswers

class Repository {
    val ref = FirebaseDatabase.getInstance().getReference("codes")
    suspend fun getCodes(): MutableList<String> {
        var childsKey = mutableListOf<String>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                childsKey = snapshot.children.map { it.key.toString() }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {
                childsKey = mutableListOf()
            }
        })
        return childsKey
    }

    suspend fun codesContain(code: String): Boolean {
        return getCodes().contains(code)
    }

    suspend fun getQuestionsWithAnswers(code: String): MutableList<QuestionWithAnswers> {
        var questions = mutableListOf<QuestionWithAnswers>()
        if (codesContain(code)) {
            ref.child(code).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.child(code).children.forEach {
                        questions.add(
                            QuestionWithAnswers(
                                it.key.toString(),
                                it.value as List<String>
                            )
                        )

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
            return questions
        }
        else return mutableListOf()
    }

    suspend fun getAnswersByQuestion(question:String,questions:MutableList<QuestionWithAnswers>):QuestionWithAnswers {
        questions.forEach {
            return if(it.question==question)
                it
            else
                QuestionWithAnswers("", mutableListOf())
        }
        return QuestionWithAnswers("", mutableListOf())

    }


}