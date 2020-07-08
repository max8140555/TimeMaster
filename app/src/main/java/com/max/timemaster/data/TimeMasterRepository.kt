package com.max.timemaster.data

import androidx.lifecycle.MutableLiveData


interface TimeMasterRepository {
    suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>>
    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>
    fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>>
    fun getLiveAllEventTime(): MutableLiveData<List<Long>>
}