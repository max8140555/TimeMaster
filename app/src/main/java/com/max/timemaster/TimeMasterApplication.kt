package com.max.timemaster

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import androidx.work.Worker
import com.max.timemaster.util.ServiceLocator
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.util.KEY_EVENT_CONTENT
import com.max.timemaster.util.KEY_EVENT_TIME
import com.max.timemaster.util.KEY_EVENT_TITLE
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class TimeMasterApplication : Application() {

    val timeMasterRepository :TimeMasterRepository
        get() = ServiceLocator.provideTasksRepository()

    companion object {
        var instance: TimeMasterApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setWork(title: String?, content: String?, time: Long) {

        val diffTime = time - Calendar.getInstance().timeInMillis - 1800000

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val eventData = workDataOf(
            KEY_EVENT_TITLE to title,
            KEY_EVENT_CONTENT to content,
            KEY_EVENT_TIME to time
        )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<com.max.timemaster.Worker>()
            .setInputData(eventData)
            .setConstraints(constraints)
            .setInitialDelay(diffTime, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(uploadWorkRequest)
    }

}
