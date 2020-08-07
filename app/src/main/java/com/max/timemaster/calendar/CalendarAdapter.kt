package com.max.timemaster.calendar

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.util.SetColorStateList
import com.max.timemaster.util.TimeUtil.stampToDateTime
import com.max.timemaster.util.UserManager
import java.util.*

class CalendarAdapter:
    ListAdapter<CalendarEvent, CalendarAdapter.CalendarEventViewHolder>(
        DiffCallback
    ) {


    class CalendarEventViewHolder(private var binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(calendarEvent: CalendarEvent) {

            val selectedStartDate = calendarEvent.dateStamp?.let { stampToDateTime(it, Locale.TAIWAN) }
            val selectedEndDate = calendarEvent.dateEndStamp?.let { stampToDateTime(it, Locale.TAIWAN) }
            val color = UserManager.myDate.value?.filter {
                it.name == calendarEvent.attendee
            }!![0].color

            binding.view.backgroundTintList = color?.let { SetColorStateList.setColorStateList(it) }
            binding.textEventTitle.text = calendarEvent.calendarTitle
            binding.textEventAttendee.text = calendarEvent.attendee
            binding.textEventContent.text = calendarEvent.calendarContent
            binding.textStartTime.text = selectedStartDate
            binding.textEndTime.text = selectedEndDate



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
    ): CalendarEventViewHolder {
        return CalendarEventViewHolder(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CalendarEventViewHolder, position: Int) {
        val events =
            getItem(position)

        holder.bind(events)
    }

}