package com.max.timemaster.data

import androidx.lifecycle.MutableLiveData


interface TimeMasterRepository {

    suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>>

    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun postDate(myDate: MyDate): Result<Boolean>

    suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean>

    suspend fun postCost(dateCost: DateCost): Result<Boolean>

    fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>>

    fun getLiveAllEventTime(): MutableLiveData<List<Long>>

    fun getLiveUser(): MutableLiveData<User>

    fun getLiveMyDate(): MutableLiveData<List<MyDate>>

    fun getLiveDateFavorite(): MutableLiveData<List<DateFavorite>>

    fun getLiveDateCost(): MutableLiveData<List<DateCost>>
}