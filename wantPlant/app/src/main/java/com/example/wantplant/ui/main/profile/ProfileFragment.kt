package com.example.wantplant.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentProfileBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.book.ManualFragment

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.profileHowtouseTv.setOnClickListener {
            (context as MainActivity)
                .supportFragmentManager.beginTransaction().replace(R.id.main_frm, ManualFragment()).commitAllowingStateLoss()
        }
        return binding.root
    }
}