package com.example.wantplant.ui.main.book

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentBookBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.profile.ProfileFragment

class BookFragment : Fragment() {
    private lateinit var binding : FragmentBookBinding
    private var flowerpotDatas = ArrayList<Flowerpot>()
    private var bookGardenNameDatas = ArrayList<BookGardenName>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        flowerpotDatas.apply {
            add(Flowerpot("화분 1", "2024.01.01 ~ 2024.02.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
            add(Flowerpot("화분 2", "2024.02.01 ~ 2024.03.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
            add(Flowerpot("화분 3", "2024.03.01 ~ 2024.04.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
            add(Flowerpot("화분 4", "2024.04.01 ~ 2024.05.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
            add(Flowerpot("화분 5", "2024.05.01 ~ 2024.06.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
            add(Flowerpot("화분 6", "2024.06.01 ~ 2024.07.01", R.drawable.img_rectangle_2, R.drawable.img_plant))
        }

        val flowerpotAdapter = BookRVAdapter(flowerpotDatas)
        binding.bookFlowerpotRv.adapter = flowerpotAdapter // 리사이클러뷰의 어뎁터 연결
        binding.bookFlowerpotRv.layoutManager = GridLayoutManager(context, 2)

        flowerpotAdapter.setItemClickListener(object: BookRVAdapter.ItemClickListener{
            override fun onItemClick() { }
        })

        bookGardenNameDatas.apply {
            add(BookGardenName("나는야 공부왕"))
            add(BookGardenName("나는야 운동왕"))
            add(BookGardenName("나는야 독서왕"))
        }

        binding.bookGardenNameTv.text = bookGardenNameDatas[0].gardenName

        val bookGardenNameRVAdapter = BookGardenNameRVAdapter(bookGardenNameDatas)
        binding.bookGardenNameRv.adapter = bookGardenNameRVAdapter // 리사이클러뷰의 어뎁터 연결

        bookGardenNameRVAdapter.setItemClickListener(object: BookGardenNameRVAdapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                // 클릭된 아이템의 위치를 전달하여 선택 처리
                bookGardenNameRVAdapter.setSelectedItemPosition(position)
                val selectedGardenName = bookGardenNameDatas[position].gardenName
                binding.bookGardenNameTv.text = selectedGardenName
            }
        })

        return binding.root
    }
}