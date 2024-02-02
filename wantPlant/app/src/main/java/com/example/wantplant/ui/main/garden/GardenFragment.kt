package com.example.wantplant.ui.main.garden

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.pot.PotRetrofitInterfaces
import com.example.wantplant.databinding.FragmentGardenBinding
import com.example.wantplant.ui.main.plant.PlantActivity
import com.example.wantplant.ui.main.selectgarden.SelectGardenActivity
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GardenFragment : Fragment() {
    private lateinit var binding : FragmentGardenBinding
    private lateinit var gardenGardenRVAdapter: GardenGardenRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(layoutInflater)

        initGardenRecyclerView()

        initPotRecyclerView(String())

        onClickListener()

        return binding.root
    }

    private fun initGardenRecyclerView() {
        val gardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        gardenGardenRVAdapter = GardenGardenRVAdapter { gardenId ->
            initPotRecyclerView(gardenId)
        }
        binding.gardenGardenRv.apply {
            adapter = gardenGardenRVAdapter
            layoutManager = gardenManager
        }

        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)

        val call = api.getGardens(page = 1, pageSize = 100)
        call.enqueue(object : Callback<GardenResponse> {
            override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                if (response.isSuccessful) {
                    // 서버에서 받아온 정원 리스트를 ID 순서대로 정렬합니다.
                    val gardenList = response.body()?.result?.gardenList?.sortedBy { it.gardenId } ?: emptyList()

                    // 정렬된 리스트에서 각 정원의 이름을 가져옵니다.
                    val gardenNames = gardenList.map { it.name }
                    val gardenIds = gardenList.map { it.gardenId.toString() }

                    // 정원 이름 설정
                    gardenGardenRVAdapter.gardenTitles = gardenNames
                    gardenGardenRVAdapter.notifyDataSetChanged()

                    // 정원의 id 저장
                    gardenGardenRVAdapter.gardenIds = gardenIds

                    Log.d("Retrofit 정원이름리스트호출", "성공: ${gardenNames}, ${gardenIds}")
                } else {
                    Log.d("Retrofit 정원이름리스트호출", "실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                Log.d("Retrofit 정원이름리스트호출", "실패: $t")
            }
        })
    }

    private fun initPotRecyclerView(gardenId: String) {
        val retrofit = getRetrofit()
        val api = retrofit.create(PotRetrofitInterfaces::class.java)

        val call = api.getPots(gardenId, 1)
        call.enqueue(object : Callback<PotsResult> {
            override fun onResponse(call: Call<PotsResult>, response: Response<PotsResult>) {
                if (response.isSuccessful) {
                    val pots = response.body()?.result?.pots ?: emptyList()

                    val potManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val adapter = GardenPotRVAdapter(pots)
                    binding.gardenPotRv.apply {
                        this.adapter = adapter
                        layoutManager = potManager
                    }
                    Log.d("Retrofit 화분", "성공 ${api.getPots(gardenId, 1)}")
                } else {
                    // 응답 실패 시의 처리를 작성합니다.
                }
            }

            override fun onFailure(call: Call<PotsResult>, t: Throwable) {
                // 요청 실패 시의 처리를 작성합니다.
            }
        })
    }


    private fun onClickListener() {

        // 정원 만들기를 눌렀을 때
        binding.gardenAddGardenTv.setOnClickListener {
            val intent = Intent(activity, SelectGardenActivity::class.java)
            startActivity(intent)
        }

        // 화분 심기를 눌렀을 때
        binding.gardenAddPotLl.setOnClickListener {
            val intent = Intent(activity, PlantActivity::class.java)
            startActivity(intent)
        }
    }

}