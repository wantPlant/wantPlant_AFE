package com.example.wantplant.ui.main.book

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.CompletedPotResult
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.garden.response.GardenGetList
import com.example.wantplant.data.remote.garden.response.GardenGetResponse
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.databinding.FragmentBookBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookFragment : Fragment() {
    private lateinit var binding : FragmentBookBinding
    private var selectedGardenTitle: String? = null // 선택된 정원 이름
    private var selectedGardenCategory: String? = null // 선택된 정원 카테고리
    private var selectedGardenDescription: String? = null // 선택된 정원 설명
    private lateinit var plantNotLayout: ConstraintLayout // 완료된 화분이 없을 경우 표시되는 레이아웃
    private lateinit var plantAllLayout: ConstraintLayout // 완료된 화분이 있을 경우 표시되는 레이아웃
    private var clickGardenId: String? = null
    private var firstGarden: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(layoutInflater)

        initGardenNameRecyclerView() // 정원 이름 리사이클러뷰 설정

        return binding.root
    }

    // 모든 정원 GET
    private fun initGardenNameRecyclerView() {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val retrofit = getRetrofit() // Retrofit 객체를 얻음
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)
        // GardenRetrofitInterfaces 인터페이스 생성 => API

        val call = api.getGarden("Bearer $accessToken")
        call.enqueue(object : Callback<GardenGetResponse> { // Call 객체에 비동기적인 응답 처리 등록
            override fun onResponse(call: Call<GardenGetResponse>, response: Response<GardenGetResponse>) {
                if (response.isSuccessful) { // 서버 응답이 성공인 경우
                    val gardenList = response.body()?.result?.gardens?.sortedBy { it.gardenId } ?: emptyList()
                    val gardenNameAdapter = response.body()?.result?.let { BookGardenNameRVAdapter(it.gardens)}
                    val gardenNameManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    // 정원 이름 리사이클러뷰 레이아웃 메니저 -> 수평으로

                    binding.bookGardenNameRv.apply {
                        adapter = gardenNameAdapter
                        layoutManager = gardenNameManager
                    } // 정원 이름 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

                    gardenNameAdapter?.setGardenClick(object: BookGardenNameRVAdapter.GardenClickListener {

                        // 정원 클릭 시
                        override fun onGardenClick(gardenId: String) {
                            clickGardenId = gardenId

                            val selectedGarden = gardenList.find { it.gardenId.toString() == clickGardenId }

                            selectedGardenTitle = selectedGarden?.name
                            binding.bookGardenNameTv.text = selectedGardenTitle
                            // 선택된 정원의 이름을 selectedGardenTitle에 저장 후 bookGardenNameTv에 표시

                            selectedGardenCategory = selectedGarden?.gardenCategory
                            binding.bookCategoryTitleTv.text = selectedGardenCategory
                            // 선택된 정원의 카테고리를 selectedGardenCategory 저장 후 bookCategoryTitleTv에 표시

                            selectedGardenDescription = selectedGarden?.description
                            binding.bookExplainGardenLabelTv.text = selectedGardenDescription
                            // 선택된 정원의 설명을 selectedGardenDescription에 저장 후 bookExplainGardenLabelTv에 표시

                            if (clickGardenId != null) {
                                initFlowerpotRecyclerView(selectedGarden!!)
                            }
                        }
                    })

                    if (gardenList.isEmpty()) { // 정원 리스트가 비어있다면
                        binding.bookCreateGardenLl.visibility = View.VISIBLE
                        binding.bookGardenNameRv.visibility = View.INVISIBLE
                    } else { // 정원이 하나라도 있다면
                        binding.bookCreateGardenLl.visibility = View.INVISIBLE
                        binding.bookGardenNameRv.visibility = View.VISIBLE

                        if (firstGarden)
                        {
                            initFlowerpotRecyclerView(gardenList[0])
                            firstGarden = false
                        }
                    }

                } else { // 서버 응답이 실패인 경우
                    Log.d("Retrofit 정원 이름 리스트 호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenGetResponse>, t: Throwable) { // 서버 응답 실패시 호출
                Log.d("Retrofit 정원 이름 리스트 호출", "실패: $t") // 예외(t)를 통해 실패한 이유를 확인
            }
        })
    }

    private fun initFlowerpotRecyclerView(garden : GardenGetList) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val retrofit = getRetrofit()
        val api = retrofit.create(PotRetrofitInterfaces::class.java)

        val call = api.getCompletedPots("Bearer $accessToken", garden.gardenId.toString()) // 정원 당 화분 리스트 조회
        call.enqueue(object : Callback<CompletedPotResult> {
            override fun onResponse(call: Call<CompletedPotResult>, response: Response<CompletedPotResult>) {
                if (response.isSuccessful) {
                    val completedPots = response.body()?.result?.pots ?: emptyList()
                    // 서버에서 받아온 완료된 화분 리스트 저장

                    plantNotLayout = binding.bookPlantNotCl // 아직 다 키운 화분이 없어요 ㅠ_ㅠ
                    plantAllLayout = binding.bookPlantAllCl // 전체 도감

                    if (completedPots.isEmpty()) { // 완료된 화분이 없을 경우
                        val potManager = GridLayoutManager(context, 2)
                        // 화분 리스트 레이아웃 메니저 -> 격자무늬(2개씩 수직으로 정렬)
                        val adapter = BookFlowerpotRVAdapter(completedPots)
                        // 화분 리사이클러뷰 어뎁터 저장

                        binding.bookFlowerpotRv.apply {
                            this.adapter = adapter
                            layoutManager = potManager
                        } // 화분 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

                        plantNotLayout.visibility = View.VISIBLE
                        plantAllLayout.visibility = View.INVISIBLE
                        // 아직 다 키운 화분이 없어요 ㅠ_ㅠ 표시
                    } else { // 완료된 화분이 있을 경우
                        plantNotLayout.visibility = View.INVISIBLE
                        plantAllLayout.visibility = View.VISIBLE
                        // 전체 도감 표시
                    }

                    Log.d("Retrofit 화분", "성공 ${api.getCompletedPots("Bearer $accessToken", clickGardenId.toString())}")
                } else {
                    Log.d("Retrofit 화분", "실패 ${response.errorBody()} ${response.code()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<CompletedPotResult>, t: Throwable) {
                Log.d("Retrofit 화분", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }
}