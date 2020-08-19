package com.max.timemaster.data.source.remote


import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.*
import com.max.timemaster.util.Logger
import com.max.timemaster.util.UserManager
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object TimeMasterRemoteDataSource : TimeMasterDataSource {
    private const val PATH_USERS = "users"
    private const val PATH_CALENDAR = "calendar"
    private const val PATH_DATE_FAVORITE = "dateFavorite"
    private const val PATH_DATE_COST = "dateCost"
    private const val PATH_DATE = "date"
    private const val KEY_CREATED_TIME = "dateStamp"

    override suspend fun getSelectEvent(
        greaterThan: Long,
        lessThan: Long
    ): Result<List<CalendarEvent>> = suspendCoroutine { continuation ->

        UserManager.userEmail?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS).document(it).collection(PATH_CALENDAR)
                .whereGreaterThan(KEY_CREATED_TIME, greaterThan)
                .whereLessThan(KEY_CREATED_TIME, lessThan)
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


    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> =
        suspendCoroutine { continuation ->
            val event = FirebaseFirestore.getInstance().collection(PATH_USERS)
            val document = UserManager.userEmail?.let { event.document(it) }

            UserManager.userEmail?.let {
                event.document(it)
                    .get()
                    .addOnSuccessListener { doc ->
                        document
                            ?.collection(PATH_CALENDAR)
                            ?.add(calendarEvent)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    UserManager.exp.value = UserManager.exp.value?.plus(10)

                                    Toast.makeText(
                                        TimeMasterApplication.instance,
                                        "Exp +10",
                                        Toast.LENGTH_SHORT
                                    ).show()

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
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
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
                        Logger.d( "已註冊過")
                    }
                }

        }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postDate(myDate: MyDate): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let { it ->
                db.document(it).collection(PATH_DATE)
                    .whereEqualTo("name", myDate.name)
                    .get()
                    .addOnSuccessListener { doc ->
                        myDate.loginDate = Calendar.getInstance().timeInMillis
                        if (doc.isEmpty) {
                            document?.collection(PATH_DATE)?.add(myDate)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

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
                            Logger.d( "已註冊過")
                        }

                    }
            }
        }

    override suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let {
                db.document(it).collection(PATH_DATE_FAVORITE)
                    .whereEqualTo("attendeeName", dateFavorite.attendeeName)
                    .whereEqualTo("favoriteTitle", dateFavorite.favoriteTitle)
                    .get()
                    .addOnSuccessListener { doc ->

                        if (doc.isEmpty) {
                            document?.collection(PATH_DATE_FAVORITE)?.add(dateFavorite)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        UserManager.exp.value = UserManager.exp.value?.plus(10)
                                        Toast.makeText(
                                            TimeMasterApplication.instance,
                                            "Exp +10",
                                            Toast.LENGTH_SHORT
                                        ).show()

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

                            for (i in doc) {
                                val list = i["favoriteContent"] as MutableList<String>

                                dateFavorite.favoriteContent?.let { it1 -> list.addAll(it1) }

                                document
                                    ?.collection(PATH_DATE_FAVORITE)
                                    ?.document(i.id)
                                    ?.update("favoriteContent", list)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {

                                            UserManager.exp.value = UserManager.exp.value?.plus(10)
                                            Toast.makeText(
                                                TimeMasterApplication.instance,
                                                "Exp +10",
                                                Toast.LENGTH_SHORT
                                            ).show()

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
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            val document = UserManager.userEmail?.let { db.document(it) }

            UserManager.userEmail?.let {
                db.document(it)
                    .get()
                    .addOnSuccessListener { doc ->
                        dateCost.time = Calendar.getInstance().timeInMillis
                        document?.collection(PATH_DATE_COST)?.add(dateCost)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    UserManager.exp.value = UserManager.exp.value?.plus(10)
                                    Toast.makeText(
                                        TimeMasterApplication.instance,
                                        "Exp +10",
                                        Toast.LENGTH_SHORT
                                    ).show()

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
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            UserManager.userEmail?.let {
                db
                    .document(it)
                    .collection(PATH_DATE)
                    .whereEqualTo("name", myDate.name)
                    .get()
                    .addOnSuccessListener { docs ->

                        for (doc in docs) {
                            db.document(it).collection(PATH_DATE).document(doc.id).update(
                                "active",
                                myDate.active,
                                "birthday",
                                myDate.birthday,
                                "color",
                                myDate.color,
                                "image",
                                myDate.image,
                                "position",
                                myDate.position
                            )
                        }

                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { exception ->
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${exception.message}")
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun upUserExp(exp: Long): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
            UserManager.userEmail?.let { it ->
                db
                    .document(it)
                    .update("exp", exp)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
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

    override suspend fun syncImage(uri: Uri): Result<String> =
        suspendCoroutine { continuation ->

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(uri)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        ref.downloadUrl.addOnSuccessListener { task ->


                            continuation.resume(Result.Success(task.toString()))

                        }
                    } else {
                        it.exception?.let {

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

    override suspend fun handleFacebookAccessToken(token: AccessToken?): Result<FirebaseUser?> =
        suspendCoroutine { continuation ->

            val credential = FacebookAuthProvider.getCredential(token?.token!!)
            FirebaseAuth.getInstance()?.signInWithCredential(credential)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Third step
                        //Login
                        val userId = task.result?.additionalUserInfo?.profile?.getValue("id")
                        UserManager.user.image = "https://graph.facebook.com/$userId/picture?height=500"
                        UserManager.userEmail = task.result?.user?.email.toString()
                        UserManager.user.email = task.result?.user?.email.toString()
                        UserManager.user.name = task.result?.user?.displayName.toString()
                        continuation.resume(Result.Success(task.result?.user))
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


    override fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>> {
        val liveData = MutableLiveData<List<CalendarEvent>>()

        UserManager.userEmail?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS).document(it).collection(PATH_CALENDAR)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

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
            .collection(PATH_USERS)
            .whereEqualTo("email", UserManager.userEmail)

            .addSnapshotListener { snapshot, exception ->

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

        val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
        db
            .whereEqualTo("email", UserManager.userEmail)

            .get()
            .addOnSuccessListener {
                UserManager.userEmail?.let {
                    db.document(it).collection(PATH_DATE)
                        .orderBy("loginDate", Query.Direction.ASCENDING)
                        .addSnapshotListener { snapshot, exception ->

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

        val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
        UserManager.userEmail?.let {
            db.document(it).collection(PATH_DATE_FAVORITE)
                .addSnapshotListener { snapshot, exception ->

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

        val db = FirebaseFirestore.getInstance().collection(PATH_USERS)
        UserManager.userEmail?.let {
            db.document(it).collection(PATH_DATE_COST)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

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

