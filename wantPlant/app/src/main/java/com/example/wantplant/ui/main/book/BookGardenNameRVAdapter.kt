package com.example.wantplant.ui.main.book

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.remote.garden.response.GardenGetList
import com.example.wantplant.databinding.ItemBookGardenNameBinding

class BookGardenNameRVAdapter (private var gardens: List<GardenGetList>) : RecyclerView.Adapter<BookGardenNameRVAdapter.ViewHolder>() {

    var currentGardenId: String? = null
    private var selectedPosition: Int = 0

    private lateinit var mGardenClickListener: GardenClickListener

    fun setGardenClick(gardenClickListener: GardenClickListener) {
        mGardenClickListener = gardenClickListener
    }

    inner class ViewHolder(val binding: ItemBookGardenNameBinding): RecyclerView.ViewHolder(binding.root)

    interface GardenClickListener {
        fun onGardenClick(gardenId: Long)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookGardenNameRVAdapter.ViewHolder {
        val binding : ItemBookGardenNameBinding = ItemBookGardenNameBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookGardenNameRVAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.itemBookGardenNameTv.text = gardens[position].name

        if (selectedPosition == position) {
            holder.binding.itemBookGardenNameTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemBookGardenNameTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemBookGardenNameTv.setTextColor(Color.BLACK)
        } else {
            holder.binding.itemBookGardenNameTv.setBackgroundResource(R.drawable.border_nonfill_graystroke_15radius)
            holder.binding.itemBookGardenNameTv.setTypeface(null, Typeface.NORMAL)
            holder.binding.itemBookGardenNameTv.setTextColor(Color.GRAY)
        }

        holder.binding.itemBookGardenNameCl.setOnClickListener {
            notifyItemChanged(selectedPosition)

            holder.binding.itemBookGardenNameTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemBookGardenNameTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemBookGardenNameTv.setTextColor(Color.BLACK)

            mGardenClickListener.onGardenClick(gardens[position].gardenId)
            selectedPosition = position

            Log.d("gardenClick", gardens[position].potList.toString())
        }
    }

    override fun getItemCount(): Int = gardens.size

}