package com.max.timemaster

import android.app.Application
import com.max.timemaster.util.ServiceLocator
import com.max.timemaster.data.TimeMasterRepository
import kotlin.properties.Delegates

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class TimeMasterApplication : Application() {

    // Depends on the flavor,
    val timeMasterRepository :TimeMasterRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: TimeMasterApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
