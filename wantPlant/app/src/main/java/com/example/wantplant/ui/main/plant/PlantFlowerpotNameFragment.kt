package com.example.wantplant.ui.main.plant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.databinding.FragmentPlantAllBinding
import com.example.wantplant.ui.main.book.PlantFlowerpotNameRVAdapter
import com.example.wantplant.ui.main.water.week.WaterWeekPotTitleRVAdapter
import com.example.wantplant.ui.main.water.week.WaterWeekRVAdapter

class PlantFlowerpotNameFragment : Fragment() {
    private lateinit var binding: FragmentPlantAllBinding
    private var plantFlowerpotNameDatas = ArrayList<PlantFlowerpotName>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantAllBinding.inflate(layoutInflater)

        plantFlowerpotNameDatas.apply {
            add(PlantFlowerpotName("화분 1"))
            add(PlantFlowerpotName("화분 2"))
            add(PlantFlowerpotName("화분 3"))
            add(PlantFlowerpotName("화분 4"))
            add(PlantFlowerpotName("화분 5"))
            add(PlantFlowerpotName("화분 6"))
        }

        binding.wholePlantFlowerpotNameEt.hint = plantFlowerpotNameDatas[0].flowerpotName

        val plantFlowerpotNameRVAdapter = PlantFlowerpotNameRVAdapter(plantFlowerpotNameDatas)
        binding.wholePlantPotNameRv.adapter = plantFlowerpotNameRVAdapter // 리사이클러뷰의 어뎁터 연결

        plantFlowerpotNameRVAdapter.setItemClickListener(object: PlantFlowerpotNameRVAdapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                // 클릭된 아이템의 위치를 전달하여 선택 처리
                plantFlowerpotNameRVAdapter.setSelectedItemPosition(position)
                val selectedFlowerpotName = plantFlowerpotNameDatas[position].flowerpotName
                binding.wholePlantFlowerpotNameEt.hint = selectedFlowerpotName
            }
        })

        return binding.root
    }
}