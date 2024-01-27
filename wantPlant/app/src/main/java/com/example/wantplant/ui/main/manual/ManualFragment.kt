package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentManualBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.google.android.material.tabs.TabLayoutMediator

class ManualFragment : Fragment() {
    private lateinit var binding : FragmentManualBinding

    private val information = arrayListOf("사용 설명서", "자주 묻는 질문")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManualBinding.inflate(inflater, container, false)
        binding.manualBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ProfileFragment()).commitAllowingStateLoss()
        }

        val manualAdapter = ManualVPAdapter(this)
        binding.manualContentVp.adapter = manualAdapter
        TabLayoutMediator(binding.manualContentTb, binding.manualContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }
}