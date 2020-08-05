package com.max.timemaster.util

import androidx.annotation.VisibleForTesting
import com.max.timemaster.data.source.remote.TimeMasterRemoteDataSource
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.data.source.DefaultTimeMasterRepository

/**
 * A Service Locator for the [StylishRepository].
 */
object ServiceLocator {

    @Volatile
    var timeMasterRepository : TimeMasterRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(): TimeMasterRepository {
        synchronized(this) {
            return timeMasterRepository
                ?: timeMasterRepository
                ?: createStylishRepository()
        }
    }

    private fun createStylishRepository(): TimeMasterRepository {
        return DefaultTimeMasterRepository(
            TimeMasterRemoteDataSource
        )
    }

}