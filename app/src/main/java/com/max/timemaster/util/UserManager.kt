package com.max.timemaster.util

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.data.MyDate
import com.max.timemaster.data.User

object UserManager {
    var user = User()
    var myDate = MutableLiveData<List<MyDate>>()

    var addDate = MutableLiveData<String>().apply { value = "" }
    var allEvent = MutableLiveData<List<CalendarEvent>>()

    var selectTime = MutableLiveData<List<CalendarEvent>>()

    var dateFavorite = MutableLiveData<List<DateFavorite>>()


    val pref = TimeMasterApplication.instance.getSharedPreferences("FbUseEmail", Context.MODE_PRIVATE)

    var userEmail: String?
        get() {
            return pref.getString("userEmail", null)
        }
        set(value) {
            // writ data to sharedperference
            val setUserEmail = pref.edit()
            setUserEmail.putString("userEmail", value).apply()
        }

}