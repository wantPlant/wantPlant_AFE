package com.example.wantplant.ui.main.plant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.Goal
import com.example.wantplant.databinding.ItemWaterWeekGoalBinding

class PlantGoalRVAdapter (private val goals: List<Goal>): RecyclerView.Adapter<PlantGoalRVAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemWaterWeekGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemGoalPlusBtnIv.setOnClickListener {
                val plantDialog = PlantDialog(it.context as AppCompatActivity, it.context as PlantDialogInterface)
                plantDialog.show()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlantGoalRVAdapter.ViewHolder {
        val binding = ItemWaterWeekGoalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantGoalRVAdapter.ViewHolder, position: Int) {
        holder.binding.itemGoalTitleTv.text = goals[position].goalTitle
        val adapter = PlantTodoRVAdapter()
        holder.binding.itemGoalTodoRv.adapter = adapter
        adapter.setData(goals[position].todoList)
    }

    override fun getItemCount(): Int {
        return goals.size
    }
}