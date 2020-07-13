package com.max.timemaster.data.source

import androidx.lifecycle.MutableLiveData
import com.max.timemaster.data.*

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation to load Stylish sources.
 */
class DefaultTimeMasterRepository(private val remoteDataSource: TimeMasterDataSource,
                                  private val localDataSource: TimeMasterDataSource
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

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        return remoteDataSource.getLiveAllEvent()
    }

    override fun getLiveAllEventTime(): MutableLiveData<List<Long>> {
        return remoteDataSource.getLiveAllEventTime()
    }

    override fun getLiveUser(): MutableLiveData<User> {
        return remoteDataSource.getLiveUser()    }
    override fun getLiveMyDate(): MutableLiveData<List<MyDate>> {
        return remoteDataSource.getLiveMyDate()
    }

}
