package com.max.timemaster.util

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.*

object UserManager {

    var user = User()

    var myDate = MutableLiveData<List<MyDate>>()

    var exp = MutableLiveData<Long>()

    var addDate = MutableLiveData<String>().apply { value = "" }

    var allEvent = MutableLiveData<List<CalendarEvent>>()

    var selectEvent = MutableLiveData<List<CalendarEvent>>()

    var dateFavorite = MutableLiveData<List<DateFavorite>>()

    var dateCost = MutableLiveData<List<DateCost>>()

    val pref = TimeMasterApplication.instance.getSharedPreferences("FbUseEmail", Context.MODE_PRIVATE)

    var userEmail: String?
        get() {
            return pref.getString("userEmail", null)
        }
        set(value) {
            // writ data to sharedPreferences
            val setUserEmail = pref.edit()
            setUserEmail.putString("userEmail", value).apply()
        }

}