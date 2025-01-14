package com.myownportfolio.quizian.UI.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import com.myownportfolio.quizian.R
import com.myownportfolio.quizian.databinding.FragmentUniqueCodeBinding

class UniqueCode : Fragment() {
    lateinit var binding: FragmentUniqueCodeBinding
    val args: UniqueCodeArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUniqueCodeBinding.inflate(layoutInflater)
        binding.uniqueCode.text = args.code

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.appName.animate().apply {
            duration = 2000
            alpha(1f)
        }.start()
        binding.goHome.setOnClickListener {
            findNavController().navigate(
                R.id.home2,
                null,
                NavOptions.Builder().setPopUpTo(R.id.home2, true)
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .setPopEnterAnim(android.R.anim.slide_out_right) // Built-in slide in from right for FragmentA
                    .setPopExitAnim(android.R.anim.slide_in_left)
                    .build()
            )
        }

        binding.copyId.setOnClickListener {
            clipBoardCopy(binding.uniqueCode.text.toString())

        }

        return binding.root

    }

    fun clipBoardCopy(text: String) {
        val clipBoard = binding.root.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("code", text)
        clipBoard.setPrimaryClip(clip)
        Snackbar.make(binding.root, "Copied", Snackbar.LENGTH_SHORT).show()
    }
}