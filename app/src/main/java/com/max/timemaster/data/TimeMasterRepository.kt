package com.max.timemaster.data




interface TimeMasterRepository {
    suspend fun getCalendarId(): Result<List<CalendarEvent>>
    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>
}