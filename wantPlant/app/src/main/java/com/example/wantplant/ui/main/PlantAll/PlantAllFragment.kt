package com.example.wantplant.ui.main.plantall

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.local.GoalResult
import com.example.wantplant.data.local.Pot
import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.data.remote.pot.request.PotPatchRequest
import com.example.wantplant.data.remote.pot.response.PotPatchResponse
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPatchCompleteRequest
import com.example.wantplant.data.remote.todo.response.TodoPatchCompleteResponse
import com.example.wantplant.databinding.FragmentPlantAllBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantAllFragment(private var gardenId : String) : Fragment(), PlantAllInterface { // 정원 방문으로 부터 정원 ID를 전달 받음
    private lateinit var binding: FragmentPlantAllBinding
    private lateinit var plantAllPotNameRVAdapter: PlantAllPotNameRVAdapter // 화분 이름 리사이클러뷰
    private var selectedPotName: String? = null // 선택된 화분 이름
    private lateinit var selectedPot: Pot // 선택된 화분

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantAllBinding.inflate(layoutInflater)

        getGardenAPI() // 특정 정원 조회 API

        initPotNameRecyclerView(gardenId) // 정원 당 모든 화분 조회

        initGoalTodoRecyclerView(String()) // 화분 당 목표 및 할 일 조회

        var editPotName = binding.wholePlantFlowerpotNameEt // 수정할 화분 이름

        editPotName.addTextChangedListener(object :
            TextWatcher { // 객체의 텍스트가 변경될 때마다 특정 작업을 수행하는 리스너 설정
            override fun beforeTextChanged( // 텍스트가 변경되기 전 호출
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged( // 텍스트가 변경될 때 호출
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) { // 텍스트 변경 이후 호출
                patchPotNameAPI(PotPatchRequest(editPotName.text.toString())) // 화분 이름 수정
            }
        })

        return binding.root
    }

    // 특정 정원 GET
    private fun getGardenAPI() {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "") // 엑세스 토큰 저장

        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)
        // Retrofit 객체로 GardenRetrofitInterfaces 인터페이스를 구현한 API 서비스 생성

        Log.d("Retrofit 정원 이름 호출", "사용된 액세스 토큰: Bearer $accessToken")

        val call = api.getGarden("Bearer $accessToken", gardenId.toInt())
        // API 호출을 위한 헤더에 "Bearer" 토큰을 추가, gardenId를 인자로 getGarden 메서드 호출

        call.enqueue(object : Callback<GardenResponse> {
            override fun onResponse(
                call: Call<GardenResponse>,
                response: Response<GardenResponse>
            ) {
                if (response.isSuccessful) { // 정원 이름 호출 성공 시
                    val gardenList = response.body()?.result?.gardens?.sortedBy { it.gardenId } ?: emptyList()
                    // 서버에서 받아온 정원 리스트를 gardenId 순으로 저장
                    val selectedGarden = gardenList.find { it.gardenId.toString() == gardenId }
                    // 정원 리스트에서 gardenId와 일치하는 선택된 정원 저장
                    binding.wholePlantCreatingGardenTv.text =  selectedGarden?.name
                    // wholePlantCreatingGardenTv에 선택된 정원 이름 표시

                    Log.d("Retrofit 정원 이름 호출", "성공")
                } else { // 정원 이름 호출 실패 시
                    Log.d("Retrofit 정원 이름 호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                Log.d("Retrofit 정원 이름 호출", "실패: $t")
            }
        })
    }

    // 정원 당 화분 이름 리스트 GET
    private fun initPotNameRecyclerView(gardenId: String) {
        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(PotRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        Log.d("Retrofit 정원 당 화분 이름 리스트 호출", "사용된 액세스 토큰: Bearer $accessToken")

        val call = api.getPotNames("Bearer $accessToken", gardenId)
        // gardenId를 인자로 getPotNames 메서드 호출

        val potManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // 화분 이름 리스트 레이아웃 메니저 -> 수평으로 정렬

        // plantAllPotNameRVAdapter 객체 생성 및 초기화
        plantAllPotNameRVAdapter = PlantAllPotNameRVAdapter { potId ->
            initGoalTodoRecyclerView(potId)
            // potId를 인자로 받아 initGoalTodoRecyclerView() 함수를 호출
        }

        binding.wholePlantWaterWeekGoalRv.apply {
            this.adapter = plantAllPotNameRVAdapter
            layoutManager = potManager
        } // 화분 이름 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<PotsResult> {
            override fun onResponse(call: Call<PotsResult>, response: Response<PotsResult>) {
                if (response.isSuccessful) {
                    val pots = response.body()?.result?.pots?.sortedBy { it.potId } ?: emptyList()
                    // 서버에서 받아온 완료된 화분 리스트를 potId 순으로 저장

                    // 정렬된 리스트에서 각 화분의 이름 및 아이디를 가져옴
                    val potNames = pots.map { it.potName } // map - 추출
                    val potIds = pots.map { it.potId.toString() }

                    plantAllPotNameRVAdapter.potTitles = potNames // 화분 이름 설정
                    plantAllPotNameRVAdapter.notifyDataSetChanged()
                    // 어댑터가 현재 데이터셋에 대한 변경 사항을 감지하고 이를 RecyclerView에 적용하도록 시스템에 알림

                    plantAllPotNameRVAdapter.potIds = potIds

                    // 화분을 선택할 때 발생하는 이벤트 처리
                    plantAllPotNameRVAdapter.setOnPotClickListener { potId ->
                        val pot = pots.find { it.potId.toString() == potId } // 화분 리스트에서 potId와 일치하는 화분 저장
                        selectedPot = pot!! // 선택된 화분으로 지정
                        selectedPotName = pot?.potName // 선택된 화분의 이름 저장
                        binding.wholePlantFlowerpotNameEt.hint = selectedPotName // 선택된 화분의 이름 표시
                        binding.wholePlantProgressChartIv.progress = pot.proceed // 선택된 화분의 진행바 표시
                        binding.wholePlantProgressChartTv.text = "${pot.proceed}%" // 선택된 화분의 진행도 표시
                    }

                    Log.d("Retrofit 화분 이름 리스트", "성공 ${api.getPotNames("Bearer $accessToken", gardenId)}")
                } else {
                    Log.d("Retrofit 화분 이름 리스트", "실패 ${response.errorBody()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<PotsResult>, t: Throwable) {
                Log.d("Retrofit 화분 이름 리스트", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }

    // 화분당 모든 목표 및 할 일 리스트 GET
    private fun initGoalTodoRecyclerView(potId: String) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")
        Log.d("Retrofit 화분당 모든 목표 및 할 일 호출", "사용된 액세스 토큰: Bearer $accessToken")

        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(GoalRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)

        val call = api.getGoal("Bearer $accessToken", potId) // 화분 당 목표 리스트 조회
        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<GoalResult> {
            override fun onResponse(call: Call<GoalResult>, response: Response<GoalResult>) {
                if (response.isSuccessful) {
                    val goals = response.body()?.result?.goalList ?: emptyList()
                    // 서버에서 받아온 완료된 목표 리스트 저장
                    val goalManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    // 목표 리스트 레이아웃 메니저 -> 수직 정렬
                    val goalAdapter = PlantAllGoalRVAdapter(goals)
                    // 목표 리사이클러뷰 어뎁터 저장

                    binding.wholePlantWaterWeekGoalRv.apply {
                        this.adapter = goalAdapter
                        layoutManager = goalManager
                    } // 목표 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

                    // 목표 추가를 위한 클릭 이벤트 처리
                    goalAdapter?.setGoalAddClick(object: PlantAllGoalRVAdapter.ItemClickListener {

                        // 목표 및 할 일 설정 시 PlantAllGoalDialog
                        override fun onTodoAddClick(goalName: String, goalId: Long) {
                            val plantAllGoalDialog = PlantAllGoalDialog(requireContext(), this@PlantAllFragment, goalName, goalId)
                            plantAllGoalDialog.show()
                        }

                        // 할 일 클릭 시 plantAllPatchDialog
                        override fun onTodoClick2(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String) {
                            val plantAllPatchDialog = PlantAllPatchDialog(requireContext(), this@PlantAllFragment, clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle)
                            plantAllPatchDialog.show()
                        }

                        // 완료되지 않은 할 일 물방울 누를 경우의 처리
                        override fun onOutlineWaterClick2(doneId: Long, doneBoolean: Boolean) {
                            patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                        }

                        // 완료된 할 일 물방울 누를 경우의 처리
                        override fun onFillWaterClick2(doneId: Long, doneBoolean: Boolean) {
                            patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                        }
                    })

                    Log.d("Retrofit 목표", "성공 ${api.getGoal("Bearer $accessToken", potId)}")
                } else { // 응답 실패 시의 처리
                    Log.d("Retrofit 목표", "실패 ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GoalResult>, t: Throwable) { // 요청 실패 시의 처리
                Log.d("Retrofit 목표", "실패 $t")
            }
        })
    }

    // 할 일 완료 수정
    private fun patchTodoComplete(doneId: Long, todoPatchCompleteRequest: TodoPatchCompleteRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")
        Log.d("Retrofit 할 일 완료 수정", "사용된 액세스 토큰: Bearer $accessToken")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)
        todoService.patchTodoComplete("Bearer $accessToken", doneId, todoPatchCompleteRequest)
            .enqueue(object: Callback<TodoPatchCompleteResponse>{
            override fun onResponse(call: Call<TodoPatchCompleteResponse>, response: Response<TodoPatchCompleteResponse>) {
                Log.d("TodoCompletePatch/ServerSuccess", response.toString())
                Log.d("TodoCompletePatchRequest", todoPatchCompleteRequest.toString())

                val resp: TodoPatchCompleteResponse? = response.body()
                when(resp?.code) {
                    "200" -> Log.d("TodoCompletePatch/Success", "TodoCompletePatch!!")
                }
            }

            override fun onFailure(call: Call<TodoPatchCompleteResponse>, t: Throwable) {
                Log.d("TodoCompletePatch/Failure", t.message.toString())
            }
        })
    }

    // 화분 이름 수정
    private fun patchPotNameAPI(potPatchRequest: PotPatchRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")
        Log.d("Retrofit 화분 이름 수정", "사용된 액세스 토큰: Bearer $accessToken")

        val potService = getRetrofit().create(PotRetrofitInterfaces::class.java)
        potService.patchPot("Bearer $accessToken", selectedPot.potId, potPatchRequest)
            .enqueue(object : Callback<PotPatchResponse> {
                override fun onResponse(
                    call: Call<PotPatchResponse>,
                    response: Response<PotPatchResponse>
                ) {
                    Log.d("TodoPatch/ServerSuccess", response.toString())
                    Log.d("TodoPatchRequest", potPatchRequest.toString())
                    val resp: PotPatchResponse? = response.body()
                    when (resp?.code) {
                        "200" -> Log.d("TodoPatch/Success", "TodoPatch!!")
                    }
                }

                override fun onFailure(call: Call<PotPatchResponse>, t: Throwable) {
                    Log.d("TodoPatch/Failure", t.message.toString())
                }
            })
    }

    // 확인 클릭 시
    override fun onCompleteClicked() {
    }

    // 수정 클릭 시
    override fun onPatchClicked() {
    }

    // 삭제 클릭 시
    override fun onDeleteClicked() {
    }
}
