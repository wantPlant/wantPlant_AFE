package com.example.wantplant.ui.main.plant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.TodoResult
import com.example.wantplant.databinding.ItemWaterWeekTodoBinding

interface TodoItemClickListener {
    fun onTodoItemClick(todo: TodoResult)
}

class PlantTodoRVAdapter() : RecyclerView.Adapter<PlantTodoViewHolder>() {

    private var data: List<TodoResult> = emptyList()
    // 어뎁터 외부의 프레그먼트에서 리스너 객체를 던져주면 됨
    private var mTodoItemClickListener: TodoItemClickListener? = null

    fun setItemClickListener(todoItemClickListener: TodoItemClickListener){
        mTodoItemClickListener = todoItemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): PlantTodoViewHolder {
        val binding = ItemWaterWeekTodoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PlantTodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantTodoViewHolder, position: Int) {
        /*holder.bind(data[position], mTodoItemClickListener)*/
    }

    override fun getItemCount(): Int {
        return data.size
    }

//    inner class ViewHolder(val binding: ItemWaterWeekTodoBinding) : RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.itemTodoTitleTv.setOnClickListener {
//                val plantDialog = PlantDialog(it.context as AppCompatActivity, it.context as PlantDialogInterface)
//                plantDialog.show()
//            }
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : List<TodoResult>){
        this.data = data
        notifyDataSetChanged()
    }
}

class PlantTodoViewHolder(val binding: ItemWaterWeekTodoBinding) : RecyclerView.ViewHolder(binding.root){
/*    fun bind(item: TodoResult, listener: TodoItemClickListener?){
        with(binding){
            itemTodoTimeTv.text = item.time
            itemTodoTitleTv.text = item.title
            itemTodoTitleTv.setOnClickListener {
                listener?.onTodoItemClick(item)
            }
        }
    }*/
}