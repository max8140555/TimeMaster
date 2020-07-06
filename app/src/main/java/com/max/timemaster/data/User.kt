package com.max.timemaster.data


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
    var calendar: List<CalendarEvent>,
    var dateCost: List<DateCost>,
    var dateLike: List<DateLike>,
    var active: Boolean
)

data class CalendarEvent(
    var calendarTitle: String? = "",
    var attendee: String? = "",
    var calendarContent: String? = "",
    var dataStamp: Long? = 0
)

data class DateCost(
    var costTitle: String,
    var costPrice: Long,
    var time: Long
)

data class DateLike(
    var likeTitle: String
)