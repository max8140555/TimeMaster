package com.max.timemaster.cost

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.databinding.ItemCostBinding
import com.max.timemaster.util.UserManager
import kotlinx.android.synthetic.main.item_cost.view.*

class CostAdapter() :
    ListAdapter<DateCost, CostAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemCostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("SetTextI18n")
        fun bind(dateCost: DateCost) {
            binding.title.text = dateCost.costTitle
            binding.time.text = "07/14"
            binding.price.text = "${dateCost.costPrice} $"
            val color = UserManager.myDate.value?.filter {
                it.name == dateCost.attendeeName
            }!![0].color
            binding.attendee.text = dateCost.attendeeName
            binding.content.text = dateCost.costContent
            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
            val colors = intArrayOf(Color.parseColor("#$color"))
            val colorsStateList = ColorStateList(states, colors)
            binding.view.backgroundTintList = colorsStateList


            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
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

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    //執行畫布

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailedEvaluationViewHolder {
        return ProductDetailedEvaluationViewHolder(
            ItemCostBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false                  //在有inflate 的地方 要注意   parnt, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    //2.綁定ViewHolder 畫布
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder
        holder.itemView.setOnClickListener {
            val itemVisibility = holder.itemView.content.visibility
            holder.itemView.content.visibility = if (itemVisibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }

        }
        holder.bind(product)
    }

}