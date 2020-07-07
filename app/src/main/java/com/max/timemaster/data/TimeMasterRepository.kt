package com.max.timemaster.data




interface TimeMasterRepository {
    suspend fun getCalendarId(greaterThan: Long ,lessThan: Long): Result<List<CalendarEvent>>
    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>
}