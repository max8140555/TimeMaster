package com.max.timemaster.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.Result
import com.max.timemaster.data.TimeMasterDataSource


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

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        TODO("Not yet implemented")
    }

    override fun getLiveAllEventTime(): MutableLiveData<List<Long>> {
        TODO("Not yet implemented")
    }


}
