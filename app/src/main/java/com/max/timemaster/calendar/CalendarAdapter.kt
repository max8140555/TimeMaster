package com.max.timemaster.calendar

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.util.TimeUtil.stampToDateTime
import com.max.timemaster.util.UserManager
import java.util.*

class CalendarAdapter() :
    ListAdapter<CalendarEvent, CalendarAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(calendarEvent: CalendarEvent) {



            val color = UserManager.myDate.value?.filter {
                it.name == calendarEvent.attendee
            }!![0].color
            binding.title.text = calendarEvent.calendarTitle
            binding.attendee.text = calendarEvent.attendee

            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
            val colors = intArrayOf(Color.parseColor("#$color"))
            val colorsStateList = ColorStateList(states, colors)
            binding.view.backgroundTintList = colorsStateList




//            binding.view.foregroundTintList = ColorStateList()
//            (Color.parseColor("#$color"))


            val selectedDate = calendarEvent.dateStamp?.let { stampToDateTime(it, Locale.TAIWAN) }
            val splitTime = selectedDate?.split(" ")
            val date = splitTime?.get(0)  // 年/月/日
            val time = splitTime?.get(1)  // 時:分+
            binding.time.text = time.toString()
            binding.content.text = calendarEvent.calendarContent


            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<CalendarEvent>() {
        override fun areItemsTheSame(
            oldItem: CalendarEvent,
            newItem: CalendarEvent
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CalendarEvent,
            newItem: CalendarEvent
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
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder

        holder.bind(product)
    }

}