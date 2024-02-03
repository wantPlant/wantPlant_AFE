package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.CompletedPotsResult
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.databinding.FragmentBookBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookFragment : Fragment() {
    private lateinit var binding : FragmentBookBinding
    private lateinit var bookGardenNameRVAdapter: BookGardenNameRVAdapter
    private var selectedGardenTitle: String? = null
    private var selectedGardenCategory: String? = null
    private var selectedGardenDescription: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(layoutInflater)

        initGardenNameRecyclerView()

        initFlowerpotRecyclerView(String())

        return binding.root
    }

    private fun initGardenNameRecyclerView() {
        val gardenNameManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // bookGardenNameRVAdapter 객체 생성 및 초기화
        bookGardenNameRVAdapter = BookGardenNameRVAdapter { gardenNameId ->
            initFlowerpotRecyclerView(gardenNameId) // gardenNameId를 인자로 받아 initFlowerpotRecyclerView() 함수를 호출
        }

        binding.bookGardenNameRv.apply {
            adapter = bookGardenNameRVAdapter
            layoutManager = gardenNameManager
        }

        val retrofit = getRetrofit() // Retrofit 객체를 얻음
        val api = retrofit.create(GardenRetrofitInterfaces::class.java) // GardenRetrofitInterfaces 인터페이스 생성 => API

        val call = api.getGardens(page = 1, pageSize = 10) // [GET] 모든 정원 조회 API
        call.enqueue(object : Callback<GardenResponse> { // Call 객체에 비동기적인 응답 처리 등록
            override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                if (response.isSuccessful) { // 서버 응답이 성공인 경우
                    val gardenList = response.body()?.result?.gardenList?.sortedBy { it.gardenId } ?: emptyList()
                    // 서버에서 받아온 정원 리스트를 ID 순서대로 정렬
                    // body() - 서버로부터 받은 데이터를 얻음
                    // ?. - 앞의 표현식이 null이면 null을 반환, 그렇지 않으면 계속 진행
                    // result?.gardenList - 서버 응답의 데이터 중 result에 해당하는 부분에서 gardenList를 가져옴
                    // ?.sortedBy { it.gardenId } - gardenId를 기준으로 gardenList 정렬 (it - 각각의 Garden 객체)
                    // ?: emptyList() - 위의 표현식이 null이면 빈 리스트 반환, response.body()가 null인 경우를 대비하여 빈 리스트로 초기화

                    // 정렬된 리스트에서 각 정원의 이름을 가져옴
                    val gardenNames = gardenList.map { it.name }
                    val gardenIds = gardenList.map { it.gardenId.toString() }

                    // 정원 이름 설정
                    bookGardenNameRVAdapter.gardenTitles = gardenNames
                    bookGardenNameRVAdapter.notifyDataSetChanged()
                    // 어댑터가 현재 데이터셋에 대한 변경 사항을 감지하고 이를 RecyclerView에 적용하도록 시스템에 알림

                    // 정원의 id 저장
                    bookGardenNameRVAdapter.gardenIds = gardenIds
                    bookGardenNameRVAdapter.setOnGardenClickListener { gardenId ->
                        val selectedGarden = gardenList.find { it.gardenId.toString() == gardenId }
                        selectedGardenTitle = selectedGarden?.name
                        binding.bookGardenNameTv.text = selectedGardenTitle
                        selectedGardenCategory = selectedGarden?.category
                        binding.bookCategoryTitleTv.text = selectedGardenCategory
                        selectedGardenDescription = selectedGarden?.description
                        binding.bookExplainGardenLabelTv.text = selectedGardenDescription
                    }

                    Log.d("Retrofit 정원이름리스트호출", "성공: ${gardenNames}, ${gardenIds}")
                } else { // 서버 응답이 실패인 경우
                    Log.d("Retrofit 정원이름리스트호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) { // 서버 응답 실패시 호출
                Log.d("Retrofit 정원이름리스트호출", "실패: $t") // 예외(t)를 통해 실패한 이유를 확인
            }
        })
    }

    private fun initFlowerpotRecyclerView(gardenId: String) {
        val retrofit = getRetrofit()
        val api = retrofit.create(PotRetrofitInterfaces::class.java)

        val call = api.getCompletedPots(gardenId) // 정원 당 화분 리스트 조회
        call.enqueue(object : Callback<CompletedPotsResult> {
            override fun onResponse(call: Call<CompletedPotsResult>, response: Response<CompletedPotsResult>) {
                if (response.isSuccessful) {
                    val completedPots = response.body()?.result?.pots ?: emptyList()
                    val potManager = GridLayoutManager(context, 2)
                    val adapter = BookFlowerpotRVAdapter(completedPots)
                    binding.bookFlowerpotRv.apply {
                        this.adapter = adapter
                        layoutManager = potManager
                    }
                    Log.d("Retrofit 화분", "성공 ${api.getCompletedPots(gardenId)}")
                } else {
                    // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<CompletedPotsResult>, t: Throwable) {
                // 요청 실패 시의 처리
            }
        })
    }

}