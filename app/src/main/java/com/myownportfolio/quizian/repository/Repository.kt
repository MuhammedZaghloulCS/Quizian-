package com.myownportfolio.quizian.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myownportfolio.quizian.pojo.QuestionWithAnswers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Repository {
    val ref = FirebaseDatabase.getInstance().getReference("codes")
    suspend fun getCodes(): MutableList<String> {
        val deferred = CompletableDeferred<MutableList<String>>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val codes = snapshot.children.map { it.key.toString() }.toMutableList()
                deferred.complete(codes)
            }

            override fun onCancelled(error: DatabaseError) {
                deferred.completeExceptionally(Exception("Firebase Error: ${error.message}"))
            }
        })
        return deferred.await()
    }

    suspend fun codesContain(code: String): Boolean {
        return getCodes().contains(code)
    }

    suspend fun getQuestionsWithAnswers(code: String): MutableList<QuestionWithAnswers> {
        val deferred = CompletableDeferred<MutableList<QuestionWithAnswers>>()
        ref.child(code).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.children.map {
                    QuestionWithAnswers(
                        it.key.toString(),
                        it.value as? List<String> ?: emptyList()
                    )
                }.toMutableList()
                deferred.complete(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                deferred.completeExceptionally(Exception("Firebase Error: ${error.message}"))
            }
        })
        return deferred.await()
    }
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


