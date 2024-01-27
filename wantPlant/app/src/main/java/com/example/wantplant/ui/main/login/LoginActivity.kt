package com.example.wantplant.ui.main.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityLoginBinding
import com.example.wantplant.databinding.ActivityMainBinding
import com.example.wantplant.databinding.FragmentLandingBinding
import com.example.wantplant.ui.main.book.BookFragment
import com.example.wantplant.ui.main.book.LandingFragment
import com.example.wantplant.ui.main.book.LandingPageFragment
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.example.wantplant.ui.main.selectgarden.SelectGardenActivity
import com.example.wantplant.ui.main.water.month.WaterMonthFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 카카오톡 로그인 버튼 누를 때
        binding.loginStartBtn.setOnClickListener {
            Log.d("click", "click_kakao")
            val intent = Intent(this, SelectGardenActivity::class.java)
            startActivity(intent)
        }
    }
}