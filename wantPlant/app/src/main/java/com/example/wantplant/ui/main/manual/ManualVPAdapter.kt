package com.example.wantplant.ui.main.book

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ManualVPAdapter (fragment:Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ManualHowtouseFragment()
            else -> ManualQnaFragment()
        }
    }
}