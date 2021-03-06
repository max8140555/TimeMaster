package com.max.timemaster.cost

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.ItemCostBinding
import com.max.timemaster.util.SetColorStateList
import com.max.timemaster.util.TimeUtil.stampToDateNoYear
import com.max.timemaster.util.UserManager
import kotlinx.android.synthetic.main.item_cost.view.*
import java.util.*

class CostAdapter:
    ListAdapter<DateCost, CostAdapter.CostItemViewHolder>(
        DiffCallback
    ) {

    class CostItemViewHolder(private var binding: ItemCostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("SetTextI18n")
        fun bind(dateCost: DateCost) {

            val color = UserManager.myDate.value?.filter {
                it.name == dateCost.attendeeName }?.get(0)?.color

            binding.title.text = dateCost.costTitle
            binding.attendee.text = dateCost.attendeeName
            binding.content.text = dateCost.costContent
            binding.price.text = "${dateCost.costPrice} 元"
            binding.time.text = dateCost.time?.let { stampToDateNoYear(it, Locale.TAIWAN) }
            binding.view.backgroundTintList = color?.let { SetColorStateList.setColorStateList(it) }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DateCost>() {
        override fun areItemsTheSame(
            oldItem: DateCost,
            newItem: DateCost
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DateCost,
            newItem: DateCost
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CostItemViewHolder {
        return CostItemViewHolder(
            ItemCostBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CostItemViewHolder, position: Int) {
        val cost =
            getItem(position)

        holder.itemView.setOnClickListener {

            val itemVisibility = holder.itemView.content.visibility

            holder.itemView.content.visibility = if (itemVisibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        holder.bind(cost)
    }

}