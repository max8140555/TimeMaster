package com.max.timemaster.data


data class User(
    var email: String = "",
    var name: String = "",
    var image: String = "",
    var lv: Long = 1,
    var exp: Long = 0,
    var titleName: String = "小釣手",
    var firstLoginTime: Long = 0,
    var date: List<MyDate>? = listOf()
)

data class MyDate(
    var name: String = "",
    var image: String? = "",
    var birthday: Long? = 0,
    var loginDate: Long? = 0,
    var calendar: List<CalendarEvent>? = listOf(),
    var dateCost: List<DateCost>? = listOf(),
    var dateLike: List<DateLike>? = listOf(),
    var active: Boolean? = true
)

data class CalendarEvent(
    var calendarTitle: String? = "",
    var attendee: String? = "",
    var calendarContent: String? = "",
    var dateStamp: Long? = 0
)

data class DateCost(
    var costGroup: List<CostContent>? = listOf()
)

data class CostContent(
    var costTitle: String? = "",
    var costPrice: Long? = 0,
    var time: Long? = 0
)

data class DateLike(
    var favoriteGroup: List<FavoriteContent>? = listOf()
)

data class FavoriteContent(
    var favoriteTitle: String? = "",
    var favoriteContent: List<String>? = listOf()
)