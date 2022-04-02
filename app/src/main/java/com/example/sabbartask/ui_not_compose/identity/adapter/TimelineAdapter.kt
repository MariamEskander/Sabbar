package com.example.sabbartask.ui_not_compose.identity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sabbartask.R
import com.example.sabbartask.databinding.ListItemTimelineBinding
import com.example.sabbartask.utils.extensions.hideView
import com.example.sabbartask.utils.extensions.showView

class TimelineAdapter(
    private var items: List<Timeline>
) : RecyclerView.Adapter<TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TimelineViewHolder {
        return TimelineViewHolder(
            ListItemTimelineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = items[position]
        holder.tv.text = item.name

        if (item.done){
            holder.img.setImageResource(R.drawable.ic_black_circle)
            holder.v.background = ContextCompat.getDrawable(holder.img.context,
            R.color.black)
            holder.tv.setTextColor(ContextCompat.getColor(holder.img.context,
                R.color.black))

        }else{
            holder.img.setImageResource(R.drawable.ic_white_circle)
            holder.v.background = ContextCompat.getDrawable(holder.img.context,
                R.color.white)
            holder.tv.setTextColor(ContextCompat.getColor(holder.img.context,
                R.color.white))
        }

        if(position == items.lastIndex)
            holder.v.hideView()
        else
            holder.v.showView()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(timeLineItems: ArrayList<Timeline>) {
        items = timeLineItems
        notifyItemRangeChanged(0,itemCount)
    }
}

class TimelineViewHolder(binding: ListItemTimelineBinding) : RecyclerView.ViewHolder(binding.root) {
    val img = binding.img
    val tv = binding.tv
    val v = binding.v
}


data class Timeline(
    var name : String,
    var done : Boolean
)

