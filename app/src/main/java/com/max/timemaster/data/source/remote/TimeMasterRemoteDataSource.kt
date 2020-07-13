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
import com.max.timemaster.data.*
import com.max.timemaster.util.Logger
import com.max.timemaster.util.UserManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Implementation of the Stylish source that from network.
 */
object TimeMasterRemoteDataSource : TimeMasterDataSource {

    private const val PATH_ARTICLES = "calendar"
    private const val KEY_CREATED_TIME = "dateStamp"
    override suspend fun getSelectEvent(
        greaterThan: Long,
        lessThan: Long
    ): Result<List<CalendarEvent>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .whereGreaterThan("dateStamp", greaterThan)
            .whereLessThan("dateStamp", lessThan)
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
//        calendarEvent.dateStamp = Calendar.getInstance().timeInMillis
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

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postUser(user: User): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { db.document(it) }

            db
                .whereEqualTo("email", UserManager.userEmail)
                .get()
                .addOnSuccessListener {
                    user.firstLoginTime = Calendar.getInstance().timeInMillis
                    if (it.isEmpty) {
                        document
                            ?.set(user)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    Logger.i("postUser: $user")

                                    continuation.resume(Result.Success(true))
                                } else {
                                    task.exception?.let {

                                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                        continuation.resume(Result.Error(it))
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Result.Fail(
                                            TimeMasterApplication.instance.getString(
                                                R.string.you_know_nothing
                                            )
                                        )
                                    )
                                }
                            }
                    } else {
                        Log.d("postUser else", "已註冊過")
                    }
                }

        }

    override suspend fun postDate(myDate: MyDate): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let {
                db.document(it).collection("date")
                    .whereEqualTo("name", myDate.name)
                    .get()
                    .addOnSuccessListener { doc ->
                        if (doc.isEmpty) {
                            document?.collection("date")?.add(myDate)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Logger.i("postMyDate: $myDate")

                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {

                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                            return@addOnCompleteListener
                                        }
                                        continuation.resume(
                                            Result.Fail(
                                                TimeMasterApplication.instance.getString(
                                                    R.string.you_know_nothing
                                                )
                                            )
                                        )
                                    }
                                }
                        } else {
                            Log.d("postDate else", "已註冊過")
                        }

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
            .whereGreaterThan("dateStamp", 0)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Long>()

                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    var stamp = document.getLong("dateStamp")
                    if (stamp != null) {
                        list.add(stamp)
                    }
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getLiveUser(): MutableLiveData<User> {
        val liveData = MutableLiveData<User>()

        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", UserManager.userEmail)

            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                var user = User()

                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val info = document.toObject(User::class.java)
                    user = info
                }

                liveData.value = user
            }
        return liveData
    }

    override fun getLiveMyDate(): MutableLiveData<List<MyDate>> {
        val liveData = MutableLiveData<List<MyDate>>()

        val db = FirebaseFirestore.getInstance().collection("users")
        Log.d("EEEMAIL","${UserManager.userEmail}")
        db
            .whereEqualTo("email", UserManager.userEmail)
            .get()
            .addOnSuccessListener { doc ->
                UserManager.userEmail?.let {
                    db.document(it).collection("date")
                        .addSnapshotListener { snapshot, exception ->

                            Logger.i("addSnapshotListener detect")

                            exception?.let {
                                Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            }

                            val list = mutableListOf<MyDate>()
                            for (document in snapshot!!) {
                                Logger.d(document.id + " => " + document.data)

                                val myDate = document.toObject(MyDate::class.java)
                                list.add(myDate)
                            }

                            liveData.value = list
                        }
                }
            }
        return liveData
    }

}

