package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.calendar.CalendarViewModel
import com.max.timemaster.data.TimeMasterRepository


@Suppress("UNCHECKED_CAST")
class CalendarViewModelFactory constructor(
    private val timeMasterRepository: TimeMasterRepository,
    private val selectDate: String?,
    private val selectAttendee: String?
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CalendarViewModel::class.java) ->
                    CalendarViewModel(timeMasterRepository,selectDate,selectAttendee)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
