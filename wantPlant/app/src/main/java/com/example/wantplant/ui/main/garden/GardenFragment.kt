package com.example.wantplant.ui.main.garden

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
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
    private var accessToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(layoutInflater)

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        accessToken = sharedPref?.getString("accessToken", "")

        initGardenRecyclerView()

        initPotRecyclerView(String())

        onClickListener()

        return binding.root
    }

    private fun initGardenRecyclerView() {
        val gardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        gardenGardenRVAdapter = GardenGardenRVAdapter { gardenId ->
            gardenGardenRVAdapter.currentGardenId = gardenId  // 아이템을 클릭할 때마다 currentGardenId를 업데이트
            initPotRecyclerView(gardenId)
        }

        binding.gardenGardenRv.apply {
            adapter = gardenGardenRVAdapter
            layoutManager = gardenManager
        }

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)

        val call = api.getGardens("Bearer $accessToken", page = 1, pageSize = 100)

        call.enqueue(object : Callback<GardenResponse> {
            override fun onResponse(call: Call<GardenResponse>, response: Response<GardenResponse>) {
                if (response.isSuccessful) {
                    // 서버에서 받아온 정원 리스트를 ID 순서대로 정렬
                    val gardenList = response.body()?.result?.gardenList?.sortedBy { it.gardenId } ?: emptyList()

                    // 정렬된 리스트에서 각 정원의 이름을 가져옴
                    val gardenNames = gardenList.map { it.name }
                    val gardenIds = gardenList.map { it.gardenId.toString() }

                    // 정원 이름 설정
                    gardenGardenRVAdapter.gardenTitles = gardenNames
                    gardenGardenRVAdapter.notifyDataSetChanged()

                    // 정원의 id 저장
                    gardenGardenRVAdapter.gardenIds = gardenIds

                    // 첫 번째 정원의 ID로 currentGardenId를 초기화합니다.
                    if (gardenIds.isNotEmpty()) {
                        gardenGardenRVAdapter.currentGardenId = gardenIds[0]
                    }

                    Log.d("Retrofit 정원이름리스트호출", "받아온 정원 리스트: $gardenList")

                    Log.d("Retrofit 정원이름리스트호출", "성공: ${gardenNames}, ${gardenIds}")
                } else {
                    Log.d("Retrofit 정원이름리스트호출", "실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                Log.d("Retrofit 정원이름리스트호출 실패", "실패: $t")
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
                    Log.d("Retrofit 화분", "실패 ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PotsResult>, t: Throwable) {
                // 요청 실패 시의 처리를 작성합니다.
                Log.e("Retrofit 화분", "요청 실패: ${t.message}")
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


        // 정원 삭제를 눌럿을 때
        binding.gardenDeleteTv.setOnClickListener {
            val gardenId = gardenGardenRVAdapter.currentGardenId
            val gardenName = gardenGardenRVAdapter.getCurrentGardenName() // 정원의 이름을 가져옵니다.
            Log.d("정원 삭제 이름", gardenName)

            // AlertDialog 생성
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_garden, null)
            val builder = AlertDialog.Builder(context).setView(dialogView)
            val alertDialog = builder.show()

            val titleTextView = dialogView.findViewById<TextView>(R.id.dialog_delete_garden_title_tv)
            titleTextView.text = "$gardenName ${titleTextView.text}"

            val messageTextView = dialogView.findViewById<TextView>(R.id.dialog_delete_garden_explain_tv)
            messageTextView.text = "$gardenName 을(를) 정말로 삭제하시겠습니까?"

            dialogView.findViewById<Button>(R.id.dialog_delete_garden_complete_btn).setOnClickListener {
                // "예" 버튼을 눌렀을 때의 동작
                gardenId?.let {
                    deleteGarden(it)
                } ?: run {
                    Log.e("GardenFragment", "No garden selected.")
                }
                alertDialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.dialog_delete_garden_cancel_btn).setOnClickListener {
                // "아니오" 버튼을 눌렀을 때의 동작
                alertDialog.dismiss()
            }
        }



    }



    private fun deleteGarden(gardenId: String) {
        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)

        val gardenIdInt = gardenId.toInt()
        val call = api.deleteGarden("Bearer $accessToken", gardenIdInt) // 토큰을 헤더에 추가
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Retrofit 정원삭제", "성공")
                    // 정원 삭제 후, 화면을 갱신하거나 다른 처리를 여기에 작성합니다.
                } else {
                    Log.d("Retrofit 정원삭제", "실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Retrofit 정원삭제", "실패: $t")
            }
        })
    }

}