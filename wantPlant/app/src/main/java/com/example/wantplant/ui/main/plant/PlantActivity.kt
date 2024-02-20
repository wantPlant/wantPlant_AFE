package com.example.wantplant.ui.main.plant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.PotTagColor
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.data.remote.pot.request.PotsPostRequest
import com.example.wantplant.data.remote.pot.response.PotsPostResponse
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import com.example.wantplant.databinding.ActivityPlantBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.week.WaterWeekGoalRVAdapter
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlantActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlantBinding
    lateinit var currentDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gardenId = intent.getLongExtra("gardenId", 0)
        val gardenTitle = intent.getStringExtra("gardenTitle")

        binding = ActivityPlantBinding.inflate(layoutInflater)

        binding.plantCreatingGardenEt.text = gardenTitle

        val calendar = Calendar.getInstance()

        currentDate = "${calendar.get(Calendar.YEAR)}-0${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
        Log.d("currentDate", currentDate)

        binding.plantCreateFlowerpotAcb.setOnClickListener{
            if (binding.plantFlowerpotNameEt.text.toString() != "") {
                var potTitle = binding.plantFlowerpotNameEt.text.toString()
                postPotsAPI(PotsPostRequest(gardenId, potTitle, potTageColor = PotTagColor.PURPLE, currentDate))
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "화분 이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        onClickListener()

        setContentView(binding.root)

    }

    private fun onClickListener() {
        binding.plantBackIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postPotsAPI(potsPostRequest: PotsPostRequest) {
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val potsService = getRetrofit().create(PotRetrofitInterfaces::class.java)

        potsService.postPots("Bearer $accessToken", potsPostRequest).enqueue(object: Callback<PotsPostResponse>{
            override fun onResponse(call: Call<PotsPostResponse>, response: Response<PotsPostResponse>) {
                Log.d("PotsPost/ServerSuccess", response.toString())
                Log.d("PotsPostRequest", potsPostRequest.toString())
                val resp: PotsPostResponse? = response.body()
                Log.d("PotsAdd", response.body()?.result.toString())
                if (response.code() == 200 && response.isSuccessful) {
                    Log.d("PotsAdd/Success", "PotsAdd!!")
                }
            }

            override fun onFailure(call: Call<PotsPostResponse>, t: Throwable) {
                Log.d("PotsAdd/Failure", t.message.toString())
            }

        })
    }


}