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

        setLogo()

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun setLogo() {
        val textView: TextView = binding.gardenLogoBigTv // binding으로부터 TextView 인스턴스 가져오기

        val originalText = textView.text.toString()

        val spannableString = SpannableString(originalText)

        val startIndex = originalText.indexOf(",")
        val endIndex = originalText.indexOf(",") + 1

        spannableString.setSpan(ForegroundColorSpan(R.color.wp_green3), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }
}