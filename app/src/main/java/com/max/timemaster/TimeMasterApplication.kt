package com.max.timemaster

import android.app.Application
import com.max.timemaster.util.ServiceLocator
import com.max.timemaster.data.TimeMasterRepository
import kotlin.properties.Delegates


class TimeMasterApplication : Application() {

    // Depends on the flavor,
    val timeMasterRepository :TimeMasterRepository
        get() = ServiceLocator.provideTasksRepository()

    companion object {
        var instance: TimeMasterApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
