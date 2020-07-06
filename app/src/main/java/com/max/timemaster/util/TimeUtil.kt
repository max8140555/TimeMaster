package com.max.timemaster.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    @JvmStatic
    fun stampToDate(time: Long, locale: Locale): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

        /// 輸出為毫秒為單位
        return simpleDateFormat.parse(date).time
    }
}