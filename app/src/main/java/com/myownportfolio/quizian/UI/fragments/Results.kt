package com.myownportfolio.quizian.UI.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentResultsBinding

class Results : Fragment() {

    val ref = FirebaseDatabase.getInstance().getReference("codes")

    lateinit var binding: FragmentResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResultsBinding.inflate(layoutInflater)
        setupUI()


        return binding.root
    }

    private fun setupUI() {
        binding.appName.animate().apply {
            duration = 2000
            alpha(1f)
        }.start()
    }

}