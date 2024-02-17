package com.example.wantplant.ui.main.plantall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.TodoResult
import com.example.wantplant.databinding.ItemWaterWeekTodoBinding

class PlantAllTodoRVAdapter(private var todo: List<TodoResult>, private var goalTitle: String) : RecyclerView.Adapter<PlantAllTodoRVAdapter.ViewHolder>() {

    interface TodoClickListener {
        fun onTodoClick(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String)
        fun onOutlineWaterClick(doneId: Long, doneBoolean: Boolean)
        fun onFillWaterClick(doneId: Long, doneBoolean: Boolean)
    }

    private lateinit var mTodoClickListener: TodoClickListener

    fun setTodoClick(todoClickListener: TodoClickListener) {
        mTodoClickListener = todoClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemWaterWeekTodoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemWaterWeekTodoTitleTv.text = todo[position].title
        holder.binding.itemWaterWeekTodoTimeTv.text = todo[position].time

        // 할 일 클릭 시
        holder.binding.itemWaterWeekTodoCl.setOnClickListener {
            mTodoClickListener.onTodoClick(todo[position].id.toLong(), todo[position].title, todo[position].date, todo[position].time, goalTitle)
        }

        // 빈 물방울 클릭 시
        holder.binding.itemWaterWeekTodoWaterOutlineIv.setOnClickListener {
            todo[position].isComplete = true
            holder.binding.itemWaterWeekTodoWaterFillIv.visibility = View.VISIBLE
            holder.binding.itemWaterWeekTodoWaterOutlineIv.visibility = View.INVISIBLE

            mTodoClickListener.onOutlineWaterClick(todo[position].id.toLong(), todo[position].isComplete)
        }

        if (todo[position].isComplete) {
            holder.binding.itemWaterWeekTodoWaterFillIv.visibility = View.VISIBLE
            holder.binding.itemWaterWeekTodoWaterOutlineIv.visibility = View.INVISIBLE
        } else {
            holder.binding.itemWaterWeekTodoWaterFillIv.visibility = View.INVISIBLE
            holder.binding.itemWaterWeekTodoWaterOutlineIv.visibility = View.VISIBLE
        }

        // 찬 물방울 클릭 시
        holder.binding.itemWaterWeekTodoWaterFillIv.setOnClickListener {
            todo[position].isComplete = false
            holder.binding.itemWaterWeekTodoWaterFillIv.visibility = View.INVISIBLE
            holder.binding.itemWaterWeekTodoWaterOutlineIv.visibility = View.VISIBLE

            mTodoClickListener.onFillWaterClick(todo[position].id.toLong(), todo[position].isComplete)
        }
    }

    override fun getItemCount(): Int {
        return todo.size
    }

    inner class ViewHolder(val binding: ItemWaterWeekTodoBinding) : RecyclerView.ViewHolder(binding.root)

}