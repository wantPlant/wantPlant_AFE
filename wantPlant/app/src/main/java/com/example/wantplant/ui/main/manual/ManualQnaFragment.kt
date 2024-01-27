package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.databinding.FragmentManualQnaBinding

class ManualQnaFragment : Fragment() {
    private lateinit var binding : FragmentManualQnaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentManualQnaBinding.inflate(inflater, container, false)

        binding.qnaQuestion01Iv.setOnClickListener {
            if (binding.qnaQuestion01ToggleIv.visibility == View.VISIBLE) {
                binding.qnaAnswer01Cl.visibility = View.VISIBLE
                binding.qnaQuestion01ToggleIv.visibility = View.INVISIBLE
                binding.qnaQuestion01ToggleInvisibleIv.visibility = View.VISIBLE
            }
            else{
                binding.qnaAnswer01Cl.visibility = View.GONE
                binding.qnaQuestion01ToggleIv.visibility = View.VISIBLE
                binding.qnaQuestion01ToggleInvisibleIv.visibility = View.INVISIBLE
            }
        }

        binding.qnaQuestion02Tv.setOnClickListener {
            if (binding.qnaQuestion02ToggleIv.visibility == View.VISIBLE) {
                binding.qnaAnswer02Cl.visibility = View.VISIBLE
                binding.qnaQuestion02ToggleIv.visibility = View.INVISIBLE
                binding.qnaQuestion02ToggleInvisibleIv.visibility = View.VISIBLE
            }
            else{
                binding.qnaAnswer02Cl.visibility = View.GONE
                binding.qnaQuestion02ToggleIv.visibility = View.VISIBLE
                binding.qnaQuestion02ToggleInvisibleIv.visibility = View.INVISIBLE
            }
        }

        binding.qnaQuestion03Tv.setOnClickListener {
            if (binding.qnaQuestion03ToggleIv.visibility == View.VISIBLE) {
                binding.qnaQuestion03ToggleIv.visibility = View.INVISIBLE
                binding.qnaQuestion03ToggleInvisibleIv.visibility = View.VISIBLE
            }
            else{
                binding.qnaQuestion03ToggleIv.visibility = View.VISIBLE
                binding.qnaQuestion03ToggleInvisibleIv.visibility = View.INVISIBLE
            }
        }
        return binding.root
    }
}