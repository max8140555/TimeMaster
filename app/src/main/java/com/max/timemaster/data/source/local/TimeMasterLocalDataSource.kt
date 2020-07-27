package com.max.timemaster.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.data.*


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation of a Stylish source as a db.
 */
class TimeMasterLocalDataSource(val context: Context) : TimeMasterDataSource {
    override suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>> {
        TODO("Not yet implemented")
    }

    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postDate(myDate: MyDate): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postCost(dateCost: DateCost): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDate(myDate: MyDate): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateExp(exp: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        TODO("Not yet implemented")
    }



    override fun getLiveUser(): MutableLiveData<User> {
        TODO("Not yet implemented")
    }

    override fun getLiveMyDate(): MutableLiveData<List<MyDate>> {
        TODO("Not yet implemented")
    }

    override fun getLiveDateFavorite(): MutableLiveData<List<DateFavorite>> {
        TODO("Not yet implemented")
    }

    override fun getLiveDateCost(): MutableLiveData<List<DateCost>> {
        TODO("Not yet implemented")
    }


}
