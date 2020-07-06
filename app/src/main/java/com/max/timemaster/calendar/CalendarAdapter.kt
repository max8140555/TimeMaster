package com.max.timemaster.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.CalendarId
import com.max.timemaster.databinding.ItemCalendarBinding

class CalendarAdapter() :
    ListAdapter<CalendarId, CalendarAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarId: CalendarId) {
            binding.title.text = calendarId.calendarTitle
            binding.context.text = calendarId.calendarContent


            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<CalendarId>() {
        override fun areItemsTheSame(
            oldItem: CalendarId,
            newItem: CalendarId
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: CalendarId,
            newItem: CalendarId
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
            ItemCalendarBinding.inflate(
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
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholderholder.itemView.setOnClickListener {
        holder.bind(product)
    }

}