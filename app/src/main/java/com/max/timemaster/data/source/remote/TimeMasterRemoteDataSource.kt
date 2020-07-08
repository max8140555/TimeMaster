package com.max.timemaster.data.source.remote


import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.Result
import com.max.timemaster.data.TimeMasterDataSource
import com.max.timemaster.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Implementation of the Stylish source that from network.
 */
object TimeMasterRemoteDataSource : TimeMasterDataSource {

    private const val PATH_ARTICLES = "calendar"
    private const val KEY_CREATED_TIME = "dataStamp"
    override suspend fun getSelectEvent(
        greaterThan: Long,
        lessThan: Long
    ): Result<List<CalendarEvent>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .whereGreaterThan("dataStamp", greaterThan)
            .whereLessThan("dataStamp", lessThan)
            .orderBy(KEY_CREATED_TIME, Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<CalendarEvent>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(CalendarEvent::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(TimeMasterApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }


    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> =
        suspendCoroutine { continuation ->
            val event = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
            val document = event.document()
//        calendarEvent.dataStamp = Calendar.getInstance().timeInMillis
            document
                .set(calendarEvent)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("postEvent: $calendarEvent")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(TimeMasterApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        val liveData = MutableLiveData<List<CalendarEvent>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<CalendarEvent>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val event = document.toObject(CalendarEvent::class.java)
                    list.add(event)
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getLiveAllEventTime(): MutableLiveData<List<Long>> {
        val liveData = MutableLiveData<List<Long>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .whereGreaterThan("dataStamp", 0)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Long>()

                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                   var stamp = document.getLong("dataStamp")
                    if (stamp != null) {
                        list.add(stamp)
                    }
                }

                liveData.value = list
            }
        return liveData
    }


}

