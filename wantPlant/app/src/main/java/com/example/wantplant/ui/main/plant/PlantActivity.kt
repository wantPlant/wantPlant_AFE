package com.example.wantplant.ui.main.plant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.data.remote.pot.request.PotPostRequest
import com.example.wantplant.data.remote.pot.response.PotPostResponse
import com.example.wantplant.databinding.ActivityPlantBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
class PlantActivity : AppCompatActivity(), PlantDialogInterface {
    private var mBinding : ActivityPlantBinding? = null
    private var gardenId : String? = null // 전달 받을 정원 ID
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPlantBinding.inflate(layoutInflater)

        getGardenAPI() // 특정 정원 조회

        onClickListener()

        setContentView(binding.root)

*/
/*        binding.plantWaterWeekGoalRv.apply {
            adapter = PlantGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }*//*


        if(intent.hasExtra("goToPlant")){
            gardenId = intent.getStringExtra("goToPlant")
        } // GardenFragment로부터 데이터(정원 ID)를 전달 받음

    }

    private fun onClickListener() {
        // 뒤로 가기 눌렀을 때
        binding.plantBackIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.plantAddGoalLl.setOnClickListener {
            val plantDialog = PlantDialog(it.context as AppCompatActivity, it.context as PlantDialogInterface)
            plantDialog.show()
        }

        // 화분 생성하기 눌렀을 때
        val plantAllIntent = Intent(this, MainActivity::class.java)
        binding.plantCreateFlowerpotAcb.setOnClickListener {
            plantAllIntent.putExtra("goToPlantAll", gardenId)
            startActivity(plantAllIntent)
            // 화면 전환 및 데이터(정원 ID) 전달, PlantActivity -> MainActivity 위의 PlantAllFragment

            // 화분/목표 리스트/투두 리스트 데이터 저장
            // postPotGoalTodoAPI(PotGoalTodoPostRequest( ))
        }
    }

    // 특정 정원 GET
    private fun getGardenAPI() {
        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)
        val call = api.getGarden(gardenId!!.toInt()) // gardenId로 특정 정원 조회
        call.enqueue(object : Callback<GardenResponse> {
            override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                if (response.isSuccessful) {
                    val gardenList = response.body()?.result?.gardens?.sortedBy { it.gardenId } ?: emptyList()
                    val selectedGarden = gardenList.find { it.gardenId.toString() == gardenId }
                    binding.plantCreatingGardenEt.text = selectedGarden?.name
                    Log.d("Retrofit 정원 이름 호출", "성공")
                } else {
                    Log.d("Retrofit 정원 이름 호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                Log.d("Retrofit 정원 이름 호출", "실패: $t")
            }
        })
    }

    // 화분/목표 리스트/투두 리스트 데이터 POST
    private fun postPotGoalTodoAPI(potPostRequest: PotPostRequest) {
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")
        val potGoalTodoService = getRetrofit().create(PotRetrofitInterfaces::class.java)
        Log.d("potGoalTodoPostRequest", potPostRequest.toString())

        potGoalTodoService.postPotGoalTodo("Bearer $accessToken", potPostRequest).enqueue(object: Callback<PotPostResponse> {
            override fun onResponse(call: Call<PotPostResponse>, response: Response<PotPostResponse>) {
                Log.d("PotGoalTodoPost/ServerSuccess", response.toString())

                val resp: PotPostResponse? = response.body()
                Log.d("PotGoalTodoAdd", "code: ${resp?.message}")

                when(resp?.code) {
                    "200" -> Log.d("PotGoalTodoAdd/Success", "PotGoalTodoAdd!!")
                }
            }

            override fun onFailure(call: Call<PotPostResponse>, t: Throwable) {
                Log.d("PotGoalTodoAdd/Failure", t.message.toString())
            }
        })
    }

    override fun onCompleteClicked() {}

    override fun onCancelClicked() {}

}*/


class PlantActivity : AppCompatActivity(), PlantDialogInterface {
    private var mBinding : ActivityPlantBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPlantBinding.inflate(layoutInflater)

        onClickListener()

        setContentView(binding.root)

//        binding.plantWaterWeekGoalRv.apply {
//            adapter = WaterWeekGoalRVAdapter()
//            layoutManager = LinearLayoutManager(context)
//        }

    }

    private fun onClickListener() {
        binding.plantBackIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCompleteClicked() {}

    override fun onCancelClicked() {}

}