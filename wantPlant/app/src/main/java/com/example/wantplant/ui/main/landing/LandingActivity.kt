package com.example.wantplant.ui.main.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentLandingBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.book.LandingPageFragment
import com.example.wantplant.ui.main.book.LandingVPAdapter
import com.example.wantplant.ui.main.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: FragmentLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                // 로그인 상태입니다. MainActivity를 시작합니다.
                Log.i("LandingPageActivity", "사용자 정보: ${user}")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // 로그아웃 상태입니다. 랜딩페이지를 보여줍니다.
                Log.w("LandingPageActivity", "로그아웃 상태")
                binding = FragmentLandingBinding.inflate(layoutInflater)
                setContentView(binding.root)

                // 변경할 랜딩 페이지 추가
                // ...

                // 하고,심으러 가기 버튼 누를 때
                binding.landingLoginStartBtn.setOnClickListener {
                    Log.d("click", "click_hagosimda")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
