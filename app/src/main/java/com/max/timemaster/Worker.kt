package com.max.timemaster

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.max.timemaster.util.KEY_EVENT_CONTENT
import com.max.timemaster.util.KEY_EVENT_TIME
import com.max.timemaster.util.KEY_EVENT_TITLE
import java.util.*

lateinit var manager: NotificationManager

class Worker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        try {

                val channel = NotificationChannel(
                    "TimeMaster",
                    "TimeMaster",
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.enableVibration(true)
                channel.enableLights(true)
                manager =
                    TimeMasterApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
             
            val title = inputData.getString(KEY_EVENT_TITLE)
            val content = inputData.getString(KEY_EVENT_CONTENT)
            val time = inputData.getLong(KEY_EVENT_TIME,0L)
            val currentTime = Calendar.getInstance().timeInMillis
            val mBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(applicationContext,"TimeMaster")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(TimeMasterApplication.instance.resources.getColor(R.color.main_color))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(currentTime)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(
                        PendingIntent.getActivity(
                            applicationContext,
                            0,
                            Intent(applicationContext, MainActivity::class.java),
                            0
                        )
                    )

            } else {

                Notification.Builder(applicationContext,"TimeMaster")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(TimeMasterApplication.instance.resources.getColor(R.color.main_color))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(time)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setWhen(currentTime)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(
                        PendingIntent.getActivity(
                            applicationContext,
                            0,
                            Intent(applicationContext, MainActivity::class.java),
                            0
                        )
                    )
            }

            val id = time / 1000
            manager.notify(id.toInt(), mBuilder.build())

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }

}
