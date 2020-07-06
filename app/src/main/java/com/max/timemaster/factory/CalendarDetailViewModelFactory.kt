package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.calendar.CalendarDetailViewModel
import com.max.timemaster.data.TimeMasterRepository

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class CalendarDetailViewModelFactory constructor(
    private val timeMasterRepository: TimeMasterRepository,
    private val selectDate: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CalendarDetailViewModel::class.java) ->
                    CalendarDetailViewModel(timeMasterRepository,selectDate)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
