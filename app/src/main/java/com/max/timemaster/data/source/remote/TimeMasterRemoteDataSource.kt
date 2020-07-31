package com.max.timemaster.data.source.remote


import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.widget.Toast
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


        UserManager.userEmail?.let {
            FirebaseFirestore.getInstance()
                .collection("users").document(it).collection("calendar")
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
                        continuation.resume(
                            Result.Fail(
                                TimeMasterApplication.instance.getString(
                                    R.string.you_know_nothing
                                )
                            )
                        )
                    }
                }
        }
    }

//        calendarEvent.dateStamp = Calendar.getInstance().timeInMillis

    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> =
        suspendCoroutine { continuation ->
            val event = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { event.document(it) }

            UserManager.userEmail?.let {
                event.document(it)
                    .get()
                    .addOnSuccessListener { doc ->
                        document
                            ?.collection("calendar")
                            ?.add(calendarEvent)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Logger.i("postEvent: $calendarEvent")
                                    UserManager.exp.value = UserManager.exp.value?.plus(10)
                                    Toast.makeText(TimeMasterApplication.instance,"Exp +10",Toast.LENGTH_SHORT).show()
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

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postDate(myDate: MyDate): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let { it ->
                db.document(it).collection("date")
                    .whereEqualTo("name", myDate.name)
                    .get()
                    .addOnSuccessListener { doc ->
                        myDate.loginDate = Calendar.getInstance().timeInMillis
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

    override suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let {
                db.document(it).collection("dateFavorite")
                    .whereEqualTo("attendeeName",dateFavorite.attendeeName)
                    .whereEqualTo("favoriteTitle",dateFavorite.favoriteTitle)
                    .get()
                    .addOnSuccessListener { doc ->

                        if (doc.isEmpty){
                            document?.collection("dateFavorite")?.add(dateFavorite)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Logger.i("postdateFavorite: $dateFavorite")
                                        UserManager.exp.value = UserManager.exp.value?.plus(10)
                                        Toast.makeText(TimeMasterApplication.instance,"Exp +10",Toast.LENGTH_SHORT).show()

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
                        }else{

                            for (i in doc){
                                val list = i["favoriteContent"] as MutableList<String>

                                dateFavorite.favoriteContent?.let { it1 -> list.addAll(it1) }

                                i["favoriteTitle"]
                                Log.e("Max","$list")
                                Log.e("Max","${i["favoriteContent"]}")
                                document
                                    ?.collection("dateFavorite")
                                    ?.document(i.id)
                                    ?.update("favoriteContent",list)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Logger.i("postdateFavorite: $dateFavorite")
                                            UserManager.exp.value = UserManager.exp.value?.plus(10)
                                            Toast.makeText(TimeMasterApplication.instance,"Exp +10",Toast.LENGTH_SHORT).show()

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
                            }
                        }
                    }
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postCost(dateCost: DateCost): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let {
                db.document(it)
                    .get()
                    .addOnSuccessListener { doc ->
                        dateCost.time = Calendar.getInstance().timeInMillis
                        document?.collection("dateCost")?.add(dateCost)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Logger.i("postEvent: $dateCost")
                                    UserManager.exp.value = UserManager.exp.value?.plus(10)
                                    Toast.makeText(TimeMasterApplication.instance,"Exp +10",Toast.LENGTH_SHORT).show()

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
                    }
            }
        }

    override suspend fun updateDate(myDate: MyDate): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            UserManager.userEmail?.let {
                db
                    .document(it)
                    .collection("date")
                    .whereEqualTo("name", myDate.name)
                    .get()
                    .addOnSuccessListener { docs ->

                        for (doc in docs) {
                            db.document(it).collection("date").document(doc.id).update(
                                "active",
                                myDate.active
                                ,
                                "birthday",
                                myDate.birthday,
                                "color",
                                myDate.color,
                                "image",
                                myDate.image
                            )
                        }

                        Logger.i("updateDate: $myDate")

                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { exception ->
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${exception.message}")
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun updateExp(exp: Long): Result<Boolean>  =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("users")
            UserManager.userEmail?.let {
                db
                    .document(it)
                    .update("exp",exp)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Logger.i("updateExp: $exp")

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
            }
        }

    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        val liveData = MutableLiveData<List<CalendarEvent>>()

        UserManager.userEmail?.let {
            FirebaseFirestore.getInstance()
                .collection("users").document(it).collection("calendar")
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
        db
            .whereEqualTo("email", UserManager.userEmail)
            .get()
            .addOnSuccessListener {
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

    override fun getLiveDateFavorite(): MutableLiveData<List<DateFavorite>> {
        val liveData = MutableLiveData<List<DateFavorite>>()

        val db = FirebaseFirestore.getInstance().collection("users")
        UserManager.userEmail?.let {
            db.document(it).collection("dateFavorite")
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("addSnapshotListener detect")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<DateFavorite>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(DateFavorite::class.java)
                        list.add(event)
                    }

                    liveData.value = list
                }
        }
        return liveData
    }

    override fun getLiveDateCost(): MutableLiveData<List<DateCost>> {
        val liveData = MutableLiveData<List<DateCost>>()

        val db = FirebaseFirestore.getInstance().collection("users")
        UserManager.userEmail?.let {
            db.document(it).collection("dateCost")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("addSnapshotListener detect")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<DateCost>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(DateCost::class.java)
                        list.add(event)
                    }

                    liveData.value = list
                }
        }
        return liveData
    }
}

