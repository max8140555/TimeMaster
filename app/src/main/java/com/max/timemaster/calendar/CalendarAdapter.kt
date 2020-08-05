package com.max.timemaster.calendar

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
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

            val selectedDate = calendarEvent.dateStamp?.let { stampToDateTime(it, Locale.TAIWAN) }
            val splitTime = selectedDate?.split(" ")

            val time = splitTime?.get(1)  // 時:分+
            binding.time.text = time.toString()

            val selectedEndDate = calendarEvent.dateEndStamp?.let { stampToDateTime(it, Locale.TAIWAN) }
            val splitEndTime = selectedEndDate?.split(" ")

            val endTime = splitEndTime?.get(1)  // 時:分+
            binding.time.text = time.toString()
            binding.endTime.text = endTime.toString()
            binding.content.text = calendarEvent.calendarContent

            binding.executePendingBindings()
        }
    }

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


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailedEvaluationViewHolder {
        return ProductDetailedEvaluationViewHolder(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)

        holder.bind(product)
    }

}