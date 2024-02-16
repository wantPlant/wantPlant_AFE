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

    // 핸들러를 위한 변수 선언
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_landing)

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

                // 하고,심으러 가기 버튼 누를 때
                binding.landingLoginStartBtn.setOnClickListener {
                    Log.d("click", "click_hagosimda")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                // 변경할 랜딩 페이지 추가
                val viewPager: ViewPager2 = binding.landingBackgroundVp
                val landingAdapter = LandingVPAdapter(this)
                viewPager.adapter = landingAdapter

                val fragment1 = LandingPageFragment(R.drawable.ic_landing_1)
                val fragment2 = LandingPageFragment(R.drawable.ic_landing_2)
                val fragment3 = LandingPageFragment(R.drawable.ic_landing_3)
                landingAdapter.addFragment(fragment1)
                landingAdapter.addFragment(fragment2)
                landingAdapter.addFragment(fragment3)

                // 핸들러와 Runnable 초기화
                handler = Handler(Looper.getMainLooper())
                runnable = Runnable {
                    // 다음 페이지로 자동으로 넘기기
                    val currentItem = viewPager.currentItem
                    val nextPage = (currentItem + 1) % landingAdapter.itemCount
                    viewPager.setCurrentItem(nextPage, true)

                    // 다음 페이지로 자동으로 넘어가기 위해 핸들러에게 다시 postDelayed 호출
                    handler.postDelayed(runnable, 1500) // 1.5초 딜레이
                }

                // 핸들러 시작
                handler.postDelayed(runnable, 1500) // 1.5초 딜레이
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {}
        // 액티비티가 종료될 때 핸들러의 동작을 중지시킴
        handler.removeCallbacks(runnable)
    }
}
