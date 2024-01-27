package com.example.wantplant.ui.main.garden

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentGardenBinding

class GardenFragment : Fragment() {
    private lateinit var binding : FragmentGardenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(layoutInflater)

        initGardenRecyclerView()

        initPotRecyclerView()

        return binding.root
    }

    private fun initGardenRecyclerView() {
        binding.gardenGardenRv.apply {
            adapter = GardenGardenRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPotRecyclerView() {
        binding.gardenPotRv.apply {
            adapter = GardenPotRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }

}