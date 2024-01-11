package com.example.wantplant.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityMainBinding
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.example.wantplant.ui.main.water.WaterFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        initBottomNavigation()

        setContentView(binding.root)
    }

    // 바텀 네비게이션 구현
    private fun initBottomNavigation(){

        binding.mainBottomNavBnv.selectedItemId = R.id.bottom_nav_water

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, WaterFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNavBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.bottom_nav_garden -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, GardenFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_nav_water -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, WaterFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ProfileFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}