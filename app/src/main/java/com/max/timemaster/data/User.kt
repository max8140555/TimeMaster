package com.max.timemaster.data

import com.squareup.moshi.Json


data class User(
    var email: String,
    var name: String,
    var image: String,
    var lv: Long,
    var exp: Long,
    var titleName: String,
    var firstLoginTime: Long,
    var date: List<MyDate>
)

data class MyDate(
    var name: String,
    var image: String,
    var birthday: Long,
    var loginDate: Long,
    var calendar: List<CalendarId>,
    var dateCost: List<DateCost>,
    var dateLike: List<DateLike>,
    var active: Boolean
)

data class CalendarId(
    var calendarTitle: String = "",
    var attendee: String? = null,
    var calendarContent: String = "",
    var createdTime: Long = 0,
    var hour: Long = 0,
    var minute: Long = 0
)

data class DateCost(
    var costTitle: String,
    var costPrice: Long,
    var time: Long
)

data class DateLike(
    var likeTitle: String
)