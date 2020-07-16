package com.max.timemaster.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    @JvmStatic
    fun stampToDateTime(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dateToStampTime(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", locale)

        /// 輸出為毫秒為單位
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun dateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        /// 輸出為毫秒為單位
        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun stampToDate(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)

        return simpleDateFormat.format(Date(time))
    }

    fun stampToDateNoYear(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("MM-dd", locale)

        return simpleDateFormat.format(Date(time))
    }
}