package com.max.timemaster.util


import com.max.timemaster.data.DateSet
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    fun stampToDateNoYear(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("MM-dd", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToDate(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToDateTime(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("HH:mm", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        /// 輸出為毫秒為單位
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun dateToStampTime(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", locale)

        /// 輸出為毫秒為單位
        return simpleDateFormat.parse(date).time
    }

    fun splitDateSet(selectDate: String, delimiters: String): DateSet {
        val selectedDate = selectDate.split(delimiters)
        return DateSet(
            year = selectedDate[0].toInt(),
            month = selectedDate[1].toInt(),
            day = selectedDate[2].toInt()
        )
    }

}