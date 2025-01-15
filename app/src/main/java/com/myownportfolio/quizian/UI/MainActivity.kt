package com.myownportfolio.quizian.UI

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myownportfolio.quizian.MVVM.QuestionViewModel
import com.myownportfolio.quizian.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val mainViewModel:QuestionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        handleDeepLink(intent)
    }



    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            val path = uri.path // e.g., "/openMain"
            if (path == "/openMain") {
                // Perform any specific action, such as showing a toast
                println("MainActivity opened via deep link!")
            }
        }
    }
}