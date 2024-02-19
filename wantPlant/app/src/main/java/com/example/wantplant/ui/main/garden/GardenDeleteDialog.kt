package com.example.wantplant.ui.main.garden

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.garden.response.GardenDeleteResponse
import com.example.wantplant.databinding.DialogDeleteGardenBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GardenDeleteDialog(context: Context, gardenInterface: GardenInterface, val gardenId: Long, val gardenTitle: String) : Dialog(context) {
    private var mBinding : DialogDeleteGardenBinding? = null
    private val binding get() = mBinding!!

    private var gardenInterface : GardenInterface? = null

    init {
        this.gardenInterface = gardenInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogDeleteGardenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogDeleteGardenNameTv.text = gardenTitle

        binding.dialogDeleteGardenCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.dialogDeleteGardenCompleteBtn.setOnClickListener {
            deleteGarden()
            dismiss()
        }
    }

    // 정원 삭제 api 연동
    private fun deleteGarden() {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val retrofit = getRetrofit()
        val api = retrofit.create(GardenRetrofitInterfaces::class.java)

        val call = api.deleteGarden("Bearer $accessToken", gardenId) // 토큰을 헤더에 추가
        call.enqueue(object : Callback<GardenDeleteResponse> {
            override fun onResponse(call: Call<GardenDeleteResponse>, response: Response<GardenDeleteResponse>) {
                Log.d("정원삭제요청", gardenId.toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        gardenInterface?.clickDialogDelete()
                    }
                    Log.d("Retrofit 정원삭제", "성공")
                    // 정원 삭제 후, 화면을 갱신하거나 다른 처리를 여기에 작성합니다.
                } else {
                    Log.d("Retrofit 정원삭제", "실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GardenDeleteResponse>, t: Throwable) {
                Log.d("Retrofit 정원삭제", "실패: $t")
            }
        })
    }
}