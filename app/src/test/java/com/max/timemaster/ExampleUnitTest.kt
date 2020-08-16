package com.max.timemaster

import android.icu.util.Calendar
import com.max.timemaster.data.DateSet
import com.max.timemaster.util.TimeUtil
import com.max.timemaster.util.TimeUtil.splitDateSet
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun splitDateSet_isCorrect() {
        val expected = DateSet(2018, 10, 10)
        val actual = splitDateSet("2018-10-10", "-")
        assertEquals(expected, actual)
    }

    @Test
    fun getLabels_isCorrect() {
         fun getLabels(): MutableList<String> {
            val cal = 1596963958000
            return mutableListOf(
                TimeUtil.stampToDateNoYear(cal - 86400000 * 3, Locale.TAIWAN)
                , TimeUtil.stampToDateNoYear(cal - 86400000 * 2, Locale.TAIWAN)
                , TimeUtil.stampToDateNoYear(cal - 86400000, Locale.TAIWAN)
                , TimeUtil.stampToDateNoYear(cal, Locale.TAIWAN)
            )
        }

        val expected = mutableListOf("08-06", "08-07", "08-08", "08-09")
        val actual = getLabels()
        assertEquals(expected, actual)
    }




}
