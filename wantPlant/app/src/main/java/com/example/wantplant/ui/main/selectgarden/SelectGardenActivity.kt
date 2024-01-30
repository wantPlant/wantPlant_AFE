package com.example.wantplant.ui.main.selectgarden

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.wantplant.R
import com.example.wantplant.data.local.GardenData
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.databinding.ActivitySelectGardenBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectGardenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectGardenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // retrofit
        val retrofit = getRetrofit()

        val api = retrofit.create(GardenRetrofitInterfaces::class.java)


        // 뒤로 가기 버튼 누를 때
        binding.selectBtnBackIv.setOnClickListener {
            Log.d("click", "click_back")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        
        // 카테고리 선택 및 EditText 입력
        val textView_study = findViewById<TextView>(R.id.select_topic_1_tv) // 공부
        val textView_hobby = findViewById<TextView>(R.id.select_topic_2_tv) // 취미
        val textView_exercise = findViewById<TextView>(R.id.select_topic_3_tv) // 운동
        val textViewList = listOf(textView_study, textView_hobby, textView_exercise) // 각 주제에 대한 리스트
        var selectedTopicId: Int? = null // 선택된 주제의 id를 저장

        // 제목과 설명을 입력받는 EditText
        val editTextTitle = findViewById<EditText>(R.id.select_titlel_et)
        val editTextExplain = findViewById<EditText>(R.id.select_explain_et)

        // 정원 만들기 버튼
        val buttonMakeGarden = findViewById<AppCompatButton>(R.id.select_makegarden_btn)

        // 폼의 완성 상태를 체크하는 함수
        fun checkFormCompletion() {
            // 모든 항목이 입력되었다면 버튼을 보이게 만듦
            if (selectedTopicId != null && editTextTitle.text.isNotBlank() && editTextExplain.text.isNotBlank()) {
                buttonMakeGarden.visibility = View.VISIBLE
            } else {
                buttonMakeGarden.visibility = View.INVISIBLE
            }
        }

        // 집중하고 싶은 순간 카테고리 클릭했을 때
        for (textView in textViewList) {
            textView.setOnClickListener { clickedView -> // 각 주제 TextView에 대해 클릭 리스너 설정
                for (item in textViewList) {
                    if (item == clickedView) { // 클릭된 아이템이라면
                        item.setTypeface(null, Typeface.BOLD)
                        item.setBackgroundResource(R.drawable.select_stroke2)
                        selectedTopicId = item.id
                    } else { // 클릭되지 않은 나머지 아이템들은
                        item.setTypeface(null, Typeface.NORMAL)
                        item.setBackgroundResource(R.drawable.select_stroke)
                    }
                }

                // 폼의 완성 상태 체크
                checkFormCompletion()
            }
        }


        // 텍스트가 변경될 때마다 호출되는 TextWatcher
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 텍스트가 변경된 후 폼의 완성 상태 체크
                checkFormCompletion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        // 제목과 설명 EditText에 TextWatcher를 설정
        editTextTitle.addTextChangedListener(textWatcher)
        editTextExplain.addTextChangedListener(textWatcher)

        // 정원 만들기 버튼 누를 때
        binding.selectMakegardenBtn.setOnClickListener {
            Log.d("click", "click_make_garden")

            // 사용자가 선택한 카테고리의 이름을 가져옵니다.
            // 카테고리 매핑을 위한 HashMap
            val categoryMap = hashMapOf("취미" to "HOBBY", "공부" to "STUDY", "운동" to "EXERCISE") // 이런 식으로 필요한 매핑을 추가해주세요.

            // 사용자가 선택한 카테고리의 이름을 가져옵니다.
            val selectedCategoryNameInKorean = findViewById<TextView>(selectedTopicId!!).text.toString()

            // 한글 카테고리 이름을 영문으로 변환합니다.
            val selectedCategoryName = categoryMap[selectedCategoryNameInKorean]

            val gardenData = GardenData(
                name = editTextTitle.text.toString(),
                description = editTextExplain.text.toString(),
                category = selectedCategoryName.toString()
            )

            val call = api.postData(gardenData)
            call.enqueue(object : Callback<GardenResponse> {
                override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                    if (response.isSuccessful) {
                        // 응답 성공 시의 처리를 작성합니다.
                        Log.d("Retrofit 정원생성", "성공: ${response.body()?.isSuccess} ${selectedCategoryName}")
                    } else {
                        // 응답 실패 시의 처리를 작성합니다.
                        Log.d("Retrofit 정원생성", "실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                    // 요청 실패 시의 처리를 작성합니다.
                    Log.d("Retrofit", "실패: $t")
                }
            })

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}