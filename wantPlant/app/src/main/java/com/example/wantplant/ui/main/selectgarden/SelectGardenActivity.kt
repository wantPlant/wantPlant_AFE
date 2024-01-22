package com.example.wantplant.ui.main.selectgarden

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityLoginBinding
import com.example.wantplant.databinding.ActivityMainBinding
import com.example.wantplant.databinding.ActivitySelectGardenBinding
import com.example.wantplant.databinding.FragmentLandingBinding
import com.example.wantplant.ui.main.book.BookFragment
import com.example.wantplant.ui.main.book.LandingFragment
import com.example.wantplant.ui.main.book.LandingPageFragment
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.login.LoginActivity
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.example.wantplant.ui.main.water.month.WaterMonthFragment

class SelectGardenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectGardenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 뒤로 가기 버튼 누를 때
        binding.selectBtnBackIv.setOnClickListener {
            Log.d("click", "click_back")
            val intent = Intent(this, GardenFragment::class.java)
            startActivity(intent)
        }

        // 집중하고 싶은 순간 카테고리 클릭했을 때
        val textView_study = findViewById<TextView>(R.id.select_topic_1_tv) // 공부
        val textView_hobby = findViewById<TextView>(R.id.select_topic_2_tv) // 취미
        val textView_exercise = findViewById<TextView>(R.id.select_topic_3_tv) // 운동

        val textViewList = listOf(textView_study, textView_hobby, textView_exercise)

        for (textView in textViewList) {
            textView.setOnClickListener { clickedView ->
                for (item in textViewList) {
                    if (item == clickedView) { // 클릭된 아이템이라면
                        item.setTypeface(null, Typeface.BOLD)
                        item.setBackgroundResource(R.drawable.select_stroke2)
                    } else { // 클릭되지 않은 나머지 아이템들은
                        item.setTypeface(null, Typeface.NORMAL)
                        item.setBackgroundResource(R.drawable.select_stroke)
                    }
                }
            }
        }

    }
}