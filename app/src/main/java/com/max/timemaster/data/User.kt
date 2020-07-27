package com.max.timemaster.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class User(
    var email: String = "",
    var name: String = "",
    var image: String = "",
    var lv: Long = 1,
    var exp: Long = 0,
    var titleName: String = "小釣手",
    var firstLoginTime: Long = 0
)
@Parcelize
data class MyDate(
    var name: String = "",
    var image: String? = "",
    var birthday: Long? = 0,
    var loginDate: Long? = 0,
    var color: String? = "",
    var position: Int? = 0,
    var active: Boolean? = true
): Parcelable

data class CalendarEvent(
    var calendarTitle: String? = "",
    var attendee: String? = "",
    var calendarContent: String? = "",
    var dateStamp: Long? = 0,
    var dateEndStamp: Long? = 0
)

data class DateCost(
    var attendeeName: String? = "",
    var costTitle: String? = "",
    var costPrice: Long? = 0,
    var costContent: String? = "",
    var time: Long? = 0
)


data class DateFavorite(
    var attendeeName: String?= "",
    var favoriteTitle: String? = "",
    var favoriteContent: List<String>? = listOf()
)
