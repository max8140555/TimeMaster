package com.max.timemaster.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.data.*


class DefaultTimeMasterRepository(private val remoteDataSource: TimeMasterDataSource
) : TimeMasterRepository {
    override suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>> {
        return remoteDataSource.getSelectEvent(greaterThan ,lessThan)
    }

    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> {
        return remoteDataSource.postEvent(calendarEvent)
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        return remoteDataSource.postUser(user)
    }

    override suspend fun postDate(myDate: MyDate): Result<Boolean> {
        return remoteDataSource.postDate(myDate)
    }

    override suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean> {
        return remoteDataSource.postFavorite(dateFavorite)
    }

    override suspend fun syncImage(uri: Uri): Result<String> {
        return remoteDataSource.syncImage(uri)
    }

    override suspend fun postCost(dateCost: DateCost): Result<Boolean> {
        return remoteDataSource.postCost(dateCost)
    }

    override suspend fun updateDate(myDate: MyDate): Result<Boolean> {
        return remoteDataSource.updateDate(myDate)
    }

    override suspend fun upUserExp(exp: Long): Result<Boolean> {
        return remoteDataSource.upUserExp(exp)
    }

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        return remoteDataSource.getLiveAllEvent()
    }

    override fun getLiveUser(): MutableLiveData<User> {
        return remoteDataSource.getLiveUser()
    }
    override fun getLiveMyDate(): MutableLiveData<List<MyDate>> {
        return remoteDataSource.getLiveMyDate()
    }

    override fun getLiveDateFavorite(): MutableLiveData<List<DateFavorite>> {
        return remoteDataSource.getLiveDateFavorite()
    }

    override fun getLiveDateCost(): MutableLiveData<List<DateCost>> {
        return remoteDataSource.getLiveDateCost()
    }

}
