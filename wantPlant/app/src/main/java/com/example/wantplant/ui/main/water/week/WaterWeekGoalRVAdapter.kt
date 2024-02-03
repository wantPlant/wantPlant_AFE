package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterWeekGoalBinding
import com.example.wantplant.ui.main.plant.PlantDialog
import com.example.wantplant.ui.main.plant.PlantDialogInterface

class WaterWeekGoalRVAdapter : RecyclerView.Adapter<WaterWeekGoalRVAdapter.ViewHolder>() {
    private val data = mapOf(
        "목표1" to listOf("할일1", "할일2"),
        "목표2" to listOf("할일1", "할일2", "할일3", "할일4"),
        "목표3" to listOf("할일1"),
        "목표4" to listOf("할일1", "할일2", "할일3"),
        "목표5" to listOf("할일1", "할일2", "할일3"),
        "목표6" to listOf("할일1", "할일2", "할일3"),
        "목표7" to listOf("할일1", "할일2", "할일3"),
        "목표8" to listOf("할일1", "할일2", "할일3")

    )
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekGoalRVAdapter.ViewHolder {
        val binding = ItemWaterWeekGoalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekGoalRVAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: ItemWaterWeekGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemGoalTitleTv.text = data.keys.elementAt(position)
            binding.itemGoalTodoRv.apply {
                adapter = WaterWeekTodoRVAdapter(data.values.elementAt(position), position)
                layoutManager = LinearLayoutManager(binding.itemGoalTodoRv.context, LinearLayoutManager.VERTICAL, false)
            }
        }

        init {
            binding.itemGoalPlusBtnIv.setOnClickListener {
                val plantDialog = PlantDialog(it.context as AppCompatActivity, it.context as PlantDialogInterface)
                plantDialog.show()
            }
        }
    }
}