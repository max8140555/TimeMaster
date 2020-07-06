package com.max.timemaster.data




interface TimeMasterRepository {
    suspend fun getCalendarId(): Result<List<CalendarId>>
}