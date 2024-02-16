package com.example.wantplant.ui.main.water.month

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.remote.tag.response.TagColor
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.databinding.ItemMonthDayTagBinding

class WaterMonthDayTagRVAdapter(private val tag: List<TagMonthGetResult>?): RecyclerView.Adapter<WaterMonthDayTagRVAdapter.ViewHolder>() {

    interface TagClickListener {
        fun onTagClick(tag: TagMonthGetResult)
    }

    private lateinit var mTagClickListener: TagClickListener

    fun setTagClick(tagClickListener: TagClickListener) {
        mTagClickListener = tagClickListener
    }

    inner class ViewHolder(val binding: ItemMonthDayTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemMonthDayTagTodo.text = tag?.get(position)?.tagName.toString()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMonthDayTagBinding = ItemMonthDayTagBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tag?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        var setColor: String

        when(tag?.get(position)?.tagColor) {
            TagColor.COLOR_1 -> setColor = "#B8CDBF"
            TagColor.COLOR_2 -> setColor = "#A9B388"
            TagColor.COLOR_3 -> setColor = "#739073"
            TagColor.COLOR_4 -> setColor = "#4F6F53"
            TagColor.COLOR_5 -> setColor = "#EDE3CE"
            TagColor.COLOR_6 -> setColor = "#D4C29E"
            TagColor.COLOR_7 -> setColor = "#AD9972"
            else -> {setColor = "#7A6740"}
        }

        // 네모
//        holder.binding.itemDayTagTodoLayout.setBackgroundColor(setColor.toColorInt())

        // 둥근거
        holder.binding.itemDayTagTodoLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(setColor))

        holder.binding.itemDayTagTodoLayout.setOnClickListener {
            mTagClickListener.onTagClick(tag?.get(position)!!)
        }
    }


}