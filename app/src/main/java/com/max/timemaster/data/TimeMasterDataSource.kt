package com.max.timemaster.data

import androidx.lifecycle.MutableLiveData


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Main entry point for accessing Stylish sources.
 */
interface TimeMasterDataSource {
    suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>>

    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>

    fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>>

    fun getLiveAllEventTime(): MutableLiveData<List<Long>>

}
