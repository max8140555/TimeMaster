package com.max.timemaster.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.max.timemaster.data.TimeMasterDataSource
import com.max.timemaster.data.source.remote.TimeMasterRemoteDataSource
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.data.source.DefaultTimeMasterRepository
import com.max.timemaster.data.source.local.TimeMasterLocalDataSource

/**
 * A Service Locator for the [StylishRepository].
 */
object ServiceLocator {

    @Volatile
    var timeMasterRepository : TimeMasterRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): TimeMasterRepository {
        synchronized(this) {
            return timeMasterRepository
                ?: timeMasterRepository
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): TimeMasterRepository {
        return DefaultTimeMasterRepository(
            TimeMasterRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): TimeMasterDataSource {
        return TimeMasterLocalDataSource(
            context
        )
    }
}