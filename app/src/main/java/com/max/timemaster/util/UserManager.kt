package com.max.timemaster.util

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.User

object UserManager {
    var user = User()

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