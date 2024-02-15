package com.example.wantplant.ui.main.plant

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.local.GoalResult
import com.example.wantplant.data.local.Pot
import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.local.TodoResult
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.data.remote.pot.request.PotPatchRequest
import com.example.wantplant.data.remote.pot.response.PotPatchResponse
import com.example.wantplant.databinding.FragmentPlantAllBinding
import com.example.wantplant.ui.main.water.week.WaterWeekGoalRVAdapter
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantAllFragment(private var gardenId : String) : Fragment(), TodoItemClickListener, PlantDialogInterface {
    private lateinit var binding: FragmentPlantAllBinding
    private lateinit var plantFlowerpotNameRVAdapter: PlantFlowerpotNameRVAdapter
    private var selectedPotName: String? = null // 선택된 화분 이름
    private lateinit var selectedPot: Pot
    private var todoAdapter: PlantTodoRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantAllBinding.inflate(layoutInflater)

        binding.wholePlantWaterWeekGoalRv.apply {
            adapter = PlantGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }

        getGardenAPI()

        initPotNameRecyclerView(String())

        initGoalTodoRecyclerView(String())

        var editPotName = binding.wholePlantFlowerpotNameEt

        editPotName.addTextChangedListener(object :
            TextWatcher { // 객체의 텍스트가 변경될 때마다 특정 작업을 수행하는 리스너 설정
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { // 텍스트가 변경되기 전 호출
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { // 텍스트가 변경될 때 호출
            }

            override fun afterTextChanged(s: Editable?) { // 텍스트 변경 이후 호출
                patchPotNameAPI(PotPatchRequest(editPotName.text.toString())) // 화분 이름 수정
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoAdapter = PlantTodoRVAdapter()
        todoAdapter?.setItemClickListener(this)

        // todo 데이터 넣으려면, 이제 todoAdatper.setData(데이터) 이렇게 해주면 됨
    }

    // 특정 정원 GET
    private fun getGardenAPI() {
        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)
        val call = api.getGarden(gardenId.toInt())
        call.enqueue(object : Callback<GardenResponse> {
            override fun onResponse(
                call: Call<GardenResponse>,
                response: Response<GardenResponse>
            ) {
                if (response.isSuccessful) {
                    val gardenList = response.body()?.result?.gardenList?.sortedBy { it.gardenId } ?: emptyList()
                    val selectedGarden = gardenList.find { it.gardenId.toString() == gardenId }
                    binding.wholePlantCreatingGardenEt.text =  selectedGarden?.name

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

    // 정원 당 화분 이름 리스트 조회
    private fun initPotNameRecyclerView(gardenId: String) {
        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(PotRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)
        val call = api.getPotNames(gardenId) // 정원 당 화분 리스트 조회

        val potManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // 화분 이름 리스트 레이아웃 메니저 -> 수평으로

        // plantFlowerpotNameRVAdapter 객체 생성 및 초기화
        plantFlowerpotNameRVAdapter = PlantFlowerpotNameRVAdapter { potId ->
            initGoalTodoRecyclerView(potId)
            // potId를 인자로 받아 initGoalTodoRecyclerView() 함수를 호출
        }

        binding.wholePlantWaterWeekGoalRv.apply {
            this.adapter = plantFlowerpotNameRVAdapter
            layoutManager = potManager
        } // 화분 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<PotsResult> {
            override fun onResponse(call: Call<PotsResult>, response: Response<PotsResult>) {
                if (response.isSuccessful) {
                    val pots = response.body()?.result?.pots?.sortedBy { it.potId } ?: emptyList()
                    // 서버에서 받아온 완료된 화분 이름 리스트를 potId 순으로 저장

                    // 정렬된 리스트에서 각 화분의 이름 및 아이디를 가져옴
                    val potNames = pots.map { it.potName } // map - 추출
                    val potIds = pots.map { it.potId.toString() }

                    plantFlowerpotNameRVAdapter.potTitles = potNames // 화분 이름 설정
                    plantFlowerpotNameRVAdapter.notifyDataSetChanged()
                    // 어댑터가 현재 데이터셋에 대한 변경 사항을 감지하고 이를 RecyclerView에 적용하도록 시스템에 알림

                    plantFlowerpotNameRVAdapter.potIds = potIds
                    plantFlowerpotNameRVAdapter.setOnPotClickListener { potId ->
                        val pot = pots.find { it.potId.toString() == potId }
                        selectedPot = pot!!
                        selectedPotName = pot?.potName
                        binding.wholePlantFlowerpotNameEt.hint = selectedPotName
                    }

                    Log.d("Retrofit 화분 이름 리스트", "성공 ${api.getPotNames(gardenId)}")
                } else {
                    Log.d("Retrofit 화분 이름 리스트", "실패 ${response.errorBody()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<PotsResult>, t: Throwable) {
                Log.d("Retrofit 화분 이름 리스트", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }

    // 모든 목표 리스트 조회
    private fun initGoalTodoRecyclerView(potId: String) {
        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(GoalRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)
        val call = api.getGoal(potId) // 화분 당 목표 리스트 조회

        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<GoalResult> {
            override fun onResponse(call: Call<GoalResult>, response: Response<GoalResult>) {
                if (response.isSuccessful) {
                    val goals = response.body()?.result?.goalList ?: emptyList()
                    // 서버에서 받아온 완료된 화분 리스트 저장
                    val goalManager = GridLayoutManager(context, 2)
                    // 화분 리스트 레이아웃 메니저 -> 격자무늬(2개씩 수직으로 정렬)
                    val goalAdapter = PlantGoalRVAdapter(goals)
                    // 화분 리사이클러뷰 어뎁터 저장

                    binding.wholePlantWaterWeekGoalRv.apply {
                        this.adapter = goalAdapter
                        layoutManager = goalManager
                    } // 화분 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

                    Log.d("Retrofit 목표", "성공 ${api.getGoal(potId)}")
                } else {
                    Log.d("Retrofit 목표", "실패 ${response.errorBody()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<GoalResult>, t: Throwable) {
                Log.d("Retrofit 목표", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }

    // 화분 이름 수정
    private fun patchPotNameAPI(potPatchRequest: PotPatchRequest) {
        val potService = getRetrofit().create(PotRetrofitInterfaces::class.java)

        potService.patchPot(selectedPot.potId, potPatchRequest)
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

    override fun onTodoItemClick(todo: TodoResult) {
        Log.d("Debugging","click!")
        // todo Dialog 띄움
        val plantPatchDialog = PlantPatchDialog(requireContext(), todo, this@PlantAllFragment)
        plantPatchDialog.show()
    }
}