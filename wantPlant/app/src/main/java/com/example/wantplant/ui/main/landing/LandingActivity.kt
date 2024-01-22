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
import com.example.wantplant.ui.main.book.LandingPageFragment
import com.example.wantplant.ui.main.book.LandingVPAdapter
import com.example.wantplant.ui.main.login.LoginActivity

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: FragmentLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 변경할 랜딩 페이지 추가
        val landingAdapter = LandingVPAdapter(this)
        landingAdapter.addFragment(LandingPageFragment(R.drawable.ic_landing_1))
        landingAdapter.addFragment(LandingPageFragment(R.drawable.ic_landing_2))
        landingAdapter.addFragment(LandingPageFragment(R.drawable.ic_landing_3))
        binding.landingBackgroundVp.adapter = landingAdapter
        binding.landingBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 패널용 ViewPager에 Indicator 설정
        val indicator = binding.homeIndicator
        indicator.setViewPager(binding.landingBackgroundVp)

        // 마지막 페이지에서 오른쪽으로 넘어가면 첫 페이지로 이동하는 무한 루프 슬라이드
        val handler = Handler(Looper.getMainLooper())
        val panelViewPager = binding.landingBackgroundVp

        val sliderRunnable = object : Runnable {
            override fun run() {
                if (panelViewPager.currentItem < landingAdapter.itemCount - 1) {
                    // 현재 페이지가 마지막 페이지보다 이전이면 다음 페이지로 이동
                    panelViewPager.currentItem = panelViewPager.currentItem + 1
                } else {
                    // 마지막 페이지에서 오른쪽으로 넘어가면 첫 번째 페이지로 이동
                    panelViewPager.currentItem = 0
                }
                handler.postDelayed(this, 3000L) // 3초마다 슬라이드
            }
        }

        handler.post(sliderRunnable) // 슬라이더 실행

        // 하고,심으러 가기 버튼 누를 때
        binding.loginStartBtn.setOnClickListener {
            Log.d("click", "click_hagosimda")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
