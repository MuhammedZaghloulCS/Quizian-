package com.myownportfolio.quizian.UI.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentHomeBinding

class Home : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val database= Firebase.database
    val myRef=database.getReference("codes")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.appName.animate().apply {
            duration=2000
            alpha(1f)
        }.start()
        search("d")
        binding.prepareQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_makeQuiz)
        }
        requireActivity().onBackPressedDispatcher.addCallback{
            onBackPress()
        }
        return binding.root
    }
    private fun onBackPress(){
        requireActivity().finish()
    }

    fun search(code:String)
    {
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val keys = snapshot.children.map { it.key }
                if (keys.contains(code))
                {
                    val action=HomeDirections.actionHome2ToShowQuiz(code)
                    findNavController().navigate(action)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}