package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.databinding.FragmentBookBinding

class BookFragment : Fragment() {
    private lateinit var binding : FragmentBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(layoutInflater)

        return binding.root
    }
}