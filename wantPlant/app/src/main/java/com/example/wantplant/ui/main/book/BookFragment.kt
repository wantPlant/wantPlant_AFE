package com.example.wantplant.ui.main.book

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
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.databinding.FragmentBookBinding
import com.example.wantplant.utils.accessToken
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookFragment : Fragment() {
    private lateinit var binding : FragmentBookBinding
    private lateinit var bookGardenNameRVAdapter: BookGardenNameRVAdapter // 정원 이름 리스트 어뎁터
    private var selectedGardenTitle: String? = null // 선택된 정원 이름
    private var selectedGardenCategory: String? = null // 선택된 정원 카테고리
    private var selectedGardenDescription: String? = null // 선택된 정원 설명
    private lateinit var plantNotLayout: ConstraintLayout // 완료된 화분이 없을 경우 표시되는 레이아웃
    private lateinit var plantAllLayout: ConstraintLayout // 완료된 화분이 있을 경우 표시되는 레이아웃

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(layoutInflater)

        initGardenNameRecyclerView() // 정원 이름 리사이클러뷰 설정

        initFlowerpotRecyclerView(String()) // 화분 리사이클러뷰 설정

        return binding.root
    }

    // 모든 정원 GET
    private fun initGardenNameRecyclerView() {
        val gardenNameManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // 정원 이름 리사이클러뷰 레이아웃 메니저 -> 수평으로

        // bookGardenNameRVAdapter 객체 생성 및 초기화
        bookGardenNameRVAdapter = BookGardenNameRVAdapter { gardenId ->
            initFlowerpotRecyclerView(gardenId)
            // gardenId를 인자로 받아 initFlowerpotRecyclerView() 함수를 호출
        }

        binding.bookGardenNameRv.apply {
            adapter = bookGardenNameRVAdapter
            layoutManager = gardenNameManager
        } // 정원 이름 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용

        val retrofit = getRetrofit() // Retrofit 객체를 얻음
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)
        // GardenRetrofitInterfaces 인터페이스 생성 => API
        val call = api.getGardens("Bearer $accessToken", page = 1, pageSize = 100) // [GET] 모든 정원 조회 API
        call.enqueue(object : Callback<GardenResponse> { // Call 객체에 비동기적인 응답 처리 등록
            override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                if (response.isSuccessful) { // 서버 응답이 성공인 경우 (뭔가가 응답이 오긴 했지만 성공인지 실팬지 따로 체크해야 됨)
                    // 서버에서 받아온 정원 리스트를 ID(gardenId) 순서대로 정렬
                    val gardenList = response.body()?.result?.gardenList?.sortedBy { it.gardenId } ?: emptyList()
                    // body() - 서버로부터 받은 데이터를 얻음
                    // ?. - 앞의 표현식이 null이면 null을 반환, 그렇지 않으면 계속 진행
                    // result?.gardenList - 서버 응답의 데이터 중 result에 해당하는 부분에서 gardenList를 가져옴
                    // ?.sortedBy { it.gardenId } - gardenId를 기준으로 gardenList 정렬 (it - 각각의 Garden 객체)
                    // ?: emptyList() - 위의 표현식이 null이면 빈 리스트 반환, response.body()가 null인 경우를 대비하여 빈 리스트로 초기화

                    // 정렬된 리스트에서 각 정원의 이름을 가져옴
                    val gardenNames = gardenList.map { it.name } // map - 추출
                    val gardenIds = gardenList.map { it.gardenId.toString() }

                    plantNotLayout = binding.bookPlantNotCl // 아직 다 키운 화분이 없어요 ㅠ_ㅠ
                    plantAllLayout = binding.bookPlantAllCl // 전체 도감

                    if (gardenList.isEmpty()) { // 정원 리스트가 비어있다면
                        binding.bookCreateGardenAcb.visibility = View.VISIBLE // 정원을 만들어주세요 표시
                        binding.bookGardenNameRv.visibility = View.INVISIBLE // 정원 리스트 숨김
                    } else { // 정원이 하나라도 있다면
                        binding.bookCreateGardenAcb.visibility = View.INVISIBLE // 정원을 만들어주세요 숨김
                        binding.bookGardenNameRv.visibility = View.VISIBLE // 정원 리스트 표시

                        bookGardenNameRVAdapter.gardenTitles = gardenNames // 정원 이름 설정
                        bookGardenNameRVAdapter.notifyDataSetChanged()
                        // 어댑터가 현재 데이터셋에 대한 변경 사항을 감지하고 이를 RecyclerView에 적용하도록 시스템에 알림

                        // 정원의 id 저장
                        bookGardenNameRVAdapter.gardenIds = gardenIds
                        bookGardenNameRVAdapter.setOnGardenClickListener { gardenId ->
                            val selectedGarden = gardenList.find { it.gardenId.toString() == gardenId }
                            // gardenList에서 현재 요소의 gardenId(선택된 정원의 id)가 gardenId와 일치하는 것을 찾아서 저장

                            selectedGardenTitle = selectedGarden?.name
                            binding.bookGardenNameTv.text = selectedGardenTitle
                            // 선택된 정원의 이름을 selectedGardenTitle에 저장 후 bookGardenNameTv에 표시

                            selectedGardenCategory = selectedGarden?.category
                            binding.bookCategoryTitleTv.text = selectedGardenCategory
                            // 선택된 정원의 카테고리를 selectedGardenCategory 저장 후 bookCategoryTitleTv에 표시

                            selectedGardenDescription = selectedGarden?.description
                            binding.bookExplainGardenLabelTv.text = selectedGardenDescription
                            // 선택된 정원의 설명을 selectedGardenDescription에 저장 후 bookExplainGardenLabelTv에 표시
                        }
                    }

                    Log.d("Retrofit 정원 이름 리스트 호출", "성공: ${gardenNames}, ${gardenIds}")
                } else { // 서버 응답이 실패인 경우
                    Log.d("Retrofit 정원 이름 리스트 호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) { // 서버 응답 실패시 호출
                Log.d("Retrofit 정원 이름 리스트 호출", "실패: $t") // 예외(t)를 통해 실패한 이유를 확인
            }
        })
    }

    // 특정 정원 당 모든 화분(이름, 이미지, 기간) GET
    private fun initFlowerpotRecyclerView(gardenId: String) {
        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(PotRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)
        val call = api.getCompletedPots(gardenId) // 정원 당 화분 리스트 조회

        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<CompletedPotResult> {
            override fun onResponse(call: Call<CompletedPotResult>, response: Response<CompletedPotResult>) {
                if (response.isSuccessful) {
                    val completedPots = response.body()?.result?.pots ?: emptyList()
                    // 서버에서 받아온 완료된 화분 리스트 저장

                    plantNotLayout = binding.bookPlantNotCl // 아직 다 키운 화분이 없어요 ㅠ_ㅠ
                    plantAllLayout = binding.bookPlantAllCl // 전체 도감

                    if (completedPots.isEmpty()) { // 완료된 화분이 없을 경우
                        plantNotLayout.visibility = View.VISIBLE
                        plantAllLayout.visibility = View.INVISIBLE
                        // 아직 다 키운 화분이 없어요 ㅠ_ㅠ 표시

                        Log.d("화분이 있는지 check", "성공")

                    } else { // 완료된 화분이 있을 경우
                        plantNotLayout.visibility = View.INVISIBLE
                        plantAllLayout.visibility = View.VISIBLE
                        // 전체 도감 표시

                        Log.d("화분이 있는지 check", "실패")

                        val potManager = GridLayoutManager(context, 2)
                        // 화분 리스트 레이아웃 메니저 -> 격자무늬(2개씩 수직으로 정렬)
                        val adapter = BookFlowerpotRVAdapter(completedPots)
                        // 화분 리사이클러뷰 어뎁터 저장

                        binding.bookFlowerpotRv.apply {
                            this.adapter = adapter
                            layoutManager = potManager
                        } // 화분 리사이클러뷰에 어뎁터 및 레이아웃 메니저 적용
                    }

                    Log.d("Retrofit 화분", "성공 ${api.getCompletedPots(gardenId)}")
                } else {
                    Log.d("Retrofit 화분", "실패 ${response.errorBody()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<CompletedPotResult>, t: Throwable) {
                Log.d("Retrofit 화분", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }
}