package com.max.timemaster.profile.detail

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.MyDate
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.databinding.ItemProfileGridBinding
import com.max.timemaster.util.TimeUtil.stampToDate
import com.max.timemaster.util.TimeUtil.stampToDateTime
import java.util.*

class ProfileItemAdapter(val onClickListener: OnClickListener) :
    ListAdapter<MyDate, ProfileItemAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemProfileGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myDate: MyDate) {
            binding.textProfileName.text = myDate.name
            binding.imageString = myDate.image
            binding.date = myDate
            binding.imageProfileMain.background.setTint(
                Color.parseColor("#${myDate.color}"))
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<MyDate>() {
        override fun areItemsTheSame(
            oldItem: MyDate,
            newItem: MyDate
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MyDate,
            newItem: MyDate
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
            ItemProfileGridBinding.inflate(
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
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val myDate =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder
        holder.itemView.setOnClickListener {
            onClickListener.onClick(myDate)
        }
        holder.bind(myDate)
    }
    class OnClickListener(val clickListener: (myDate: MyDate) -> Unit) {
        fun onClick(myDate: MyDate) = clickListener(myDate)

    }
}