package com.example.wantplant.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityMainBinding
import com.example.wantplant.ui.main.book.BookFragment
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.login.LoginActivity
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 상태 확인
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)
        if (accessToken == null) {
            // 로그인 되지 않은 상태, LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
            return
        }

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            initBottomNavigation()
            setContentView(binding.root)

        } catch (e: Exception) {
            Log.e("메인액티비티", "MainActivity 초기화 중 예외 발생: ${e.message}")
            AlertDialog.Builder(this)
                .setTitle("오류 발생")
                .setMessage("앱을 다시 시작해 주세요.")
                .setPositiveButton("확인") { _, _ ->
                    finish()
                }
                .show()
        }
    }

    // 바텀 네비게이션 구현
    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, GardenFragment())
            .commitAllowingStateLoss()

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("PROFILE", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("PROFILE", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필 링크: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                binding.mainBottomNavBnv.menu.findItem(R.id.bottom_nav_profile).title = user.kakaoAccount?.profile?.nickname
            }
        }


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
                        .replace(R.id.main_frm, WaterMonthFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_nav_book -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, BookFragment())
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

    override fun onResume() {
        super.onResume()

        // 로그인 상태 확인
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("accessToken", null)
        if (accessToken == null) {
            // 로그인 되지 않은 상태, LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

}