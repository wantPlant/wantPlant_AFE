package com.example.wantplant.ui.main.plantAll

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wantplant.R
import com.example.wantplant.data.local.Pot
import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.garden.request.GardenPutRequest
import com.example.wantplant.data.remote.garden.response.GardenPutResponse
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.goal.response.GoalGetResponse
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.data.remote.pot.request.PotsPatchRequest
import com.example.wantplant.data.remote.pot.response.PotsPatchResponse
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPatchCompleteRequest
import com.example.wantplant.data.remote.todo.response.TodoPatchCompleteResponse
import com.example.wantplant.databinding.FragmentPlantAllBinding
import com.example.wantplant.ui.main.water.week.WaterWeekGoalDialog
import com.example.wantplant.ui.main.water.week.WaterWeekGoalPatchDialog
import com.example.wantplant.ui.main.water.week.WaterWeekGoalTodoDialog
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class PlantAllFragment : Fragment(), PlantAllInterface {
    private lateinit var binding: FragmentPlantAllBinding
    private var gardenId: Long = 0
    private var gardenTitle: String = ""
    private var gardenDes: String = ""
    private var clickPotId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantAllBinding.inflate(layoutInflater)

        gardenId = arguments?.getLong("gardenId")!!
        gardenTitle = arguments?.getString("gardenTitle")!!
        gardenDes = arguments?.getString("gardenDes")!!

        // 정원 이름 설정
        binding.wholePlantCreatingGardenEt.setText(gardenTitle)

        binding.wholePlantAddGoalLl.setOnClickListener {
            val plantAllGoalDialog = PlantAllGoalDialog(requireContext(), this@PlantAllFragment, clickPotId)
            plantAllGoalDialog.show()
        }

        // 화분 가져오기
        getPots()

        return binding.root
    }

    // 화분 GET api 연동
    private fun getPots() {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val potService = getRetrofit().create(PotRetrofitInterfaces::class.java)

        potService.getPots("Bearer $accessToken", gardenId, 1).enqueue(object: Callback<PotsResult>{
            override fun onResponse(call: Call<PotsResult>, response: Response<PotsResult>) {
                Log.e("PotsGet/ServerSuccess", response.message())
                if (response.code() == 200 && response.isSuccessful) {

                    if (response.body()?.result?.pots!!.isNotEmpty()) {
                        clickPotId = response.body()?.result?.pots!![0].potId

                        var potList = response.body()!!.result.pots

                        binding.wholePlantFlowerpotNameEt.setText(potList[0].potName)

                        binding.wholePlantProgressChartIv.progress = potList[0].proceed

                        binding.wholePlantProgressChartTv.text = "${potList[0].proceed}/30"

                        context?.let {
                            Glide.with(it)
                                .load(potList[0].potImageUrl)  // potImageUrl은 이미지의 URL이어야 합니다.
                                .into(binding.wholePlantPotIv)
                        }

                        getGoalTodo(clickPotId)

                    }

                    val potsManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val potsAdapter = response.body()?.result?.pots?.let { PlantAllPotRVAdapter(it) }

                    binding.wholePlantPotNameRv.apply {
                        adapter = potsAdapter
                        layoutManager = potsManager
                    }

                    potsAdapter?.setPotClick(object: PlantAllPotRVAdapter.PotClickListener{
                        override fun onPotClick(potInfo: Pot) {
                            // 이미지 설정
                            context?.let {
                                Glide.with(it)
                                    .load(potInfo.potImageUrl)  // potImageUrl은 이미지의 URL이어야 합니다.
                                    .into(binding.wholePlantPotIv)
                            }
                            // 화분 이름 설정
                            binding.wholePlantFlowerpotNameEt.setText(potInfo.potName)

                            binding.wholePlantProgressChartIv.progress = potInfo.proceed

                            binding.wholePlantProgressChartTv.text = "${potInfo.proceed}/30"

                            clickPotId = potInfo.potId

                            // 목표, 투두 가져오기
                            getGoalTodo(clickPotId)
                        }
                    })
                }
            }

            override fun onFailure(call: Call<PotsResult>, t: Throwable) {
                Log.e("PotsGet/Failure", t.message.toString())
            }
        })
    }

    private fun getGoalTodo(potId: Long) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.getPotGoalTodo("Bearer $accessToken", potId).enqueue(object: Callback<GoalGetResponse>{
            override fun onResponse(call: Call<GoalGetResponse>, response: Response<GoalGetResponse>) {
                Log.d("화분 한번에 보기/목표 서버 성공", response.message())
                if (response.isSuccessful) {
                    // 목표 리사이클러뷰 연동
                    val goalManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    val goalAdapter = response.body()?.result?.let { PlantAllGoalRVAdapter(it.goalList) }
                    binding.wholePlantGoalRv.apply {
                        adapter = goalAdapter
                        layoutManager = goalManager
                    }

                    goalAdapter?.setGoalAddClick(object: PlantAllGoalRVAdapter.ItemClickListener{

                        // 할 일 추가 클릭 시 dialog
                        override fun onTodoAddClick(goalName: String, goalId: Long) {
                            val plantAllGoalTodoDialog = PlantAllGoalTodoDialog(requireContext(), this@PlantAllFragment, goalName, goalId)
                            plantAllGoalTodoDialog.show()
                        }

                        // 목표 클릭 시
                        override fun onGoalDeleteClick(goalName: String, goalId: Long) {
                            val plantAllGoalDeleteDialog = PlantAllGoalDeleteDialog(requireContext(), this@PlantAllFragment, goalName, goalId)
                            plantAllGoalDeleteDialog.show()
                        }

                        // 할 일 클릭 시 dialog
                        override fun onTodoClick2(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String, clickGoalId: Long) {
                            val plantAllGoalPatchDialog = PlantAllGoalPatchDialog(requireContext(), this@PlantAllFragment, clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle, clickGoalId)
                            plantAllGoalPatchDialog.show()
                        }

                        // 물방울 수정
                        override fun onOutlineWaterClick2(doneId: Long, doneBoolean: Boolean) {
                            patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                        }

                        // 물방울 수정
                        override fun onFillWaterClick2(doneId: Long, doneBoolean: Boolean) {
                            patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                        }
                    })
                }
            }

            override fun onFailure(call: Call<GoalGetResponse>, t: Throwable) {
                Log.d("화분한번에보기/Failure", t.message.toString())
            }
        })
    }

    // 할 일 완료 수정 api 연동
    private fun patchTodoComplete(doneId: Long, todoPatchCompleteRequest: TodoPatchCompleteRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)

        todoService.patchTodoComplete("Bearer $accessToken", doneId, todoPatchCompleteRequest).enqueue(object: Callback<TodoPatchCompleteResponse>{
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

    override fun onCompleteClicked() {
        getGoalTodo(clickPotId)
    }

    override fun onPatchClicked() {
        getGoalTodo(clickPotId)
    }

    override fun onDeleteClicked() {
        getGoalTodo(clickPotId)
    }

    override fun onDestroy() {
        super.onDestroy()
        var gardenName = binding.wholePlantCreatingGardenEt.text.toString()
        var potName = binding.wholePlantFlowerpotNameEt.text.toString()
        patchGarden(GardenPutRequest(gardenId, gardenName, gardenDes))
        patchPot(clickPotId, PotsPatchRequest(potName))
    }

    // 정원 수정 api 연동
    private fun patchGarden(gardenPutRequest: GardenPutRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val gardenService = getRetrofit().create(GardenRetrofitInterfaces::class.java)

        gardenService.putGarden("Bearer $accessToken", gardenPutRequest).enqueue(object : Callback<GardenPutResponse>{
            override fun onResponse(call: Call<GardenPutResponse>, response: Response<GardenPutResponse>) {
                Log.d("GardenPatch/ServerSuccess", response.toString())
                Log.d("GardenPatchRequest", gardenPutRequest.toString())

                val resp: GardenPutResponse? = response.body()
                when(resp?.code) {
                    "200" -> Log.d("GardenPatch/Success", "GardenPatch!!")
                }
            }

            override fun onFailure(call: Call<GardenPutResponse>, t: Throwable) {
                Log.d("GardenPatch/Failure", t.message.toString())
            }

        })
    }

    // 화분 수정 api 연동
    private fun patchPot(potId: Long, potsPatchRequest: PotsPatchRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val potsService = getRetrofit().create(PotRetrofitInterfaces::class.java)

        potsService.patchPots("Bearer $accessToken", potId, potsPatchRequest).enqueue(object : Callback<PotsPatchResponse>{
            override fun onResponse(call: Call<PotsPatchResponse>, response: Response<PotsPatchResponse>) {
                Log.d("PotPatch/ServerSuccess", response.toString())
                Log.d("PotPatchRequest", potsPatchRequest.toString())
                Log.d("PotPatchRequest", potId.toString())
                val resp: PotsPatchResponse? = response.body()
                when(resp?.code) {
                    "200" -> Log.d("PotPatch/Success", "PotPatch!!")
                }
            }

            override fun onFailure(call: Call<PotsPatchResponse>, t: Throwable) {
                Log.d("PotPatch/Failure", t.message.toString())
            }

        })
    }
}