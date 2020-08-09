package com.max.timemaster.cost

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.DateCost
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil
import com.max.timemaster.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

private const val DAY_TIME_IN_MILLIS: Long = 86400000

class CostViewModel(private val timeMasterRepository: TimeMasterRepository) : ViewModel() {

    var filterDayPrice = listOf<Long?>()

    private var _dateCost = MutableLiveData<List<DateCost>>()

    val dateCost: LiveData<List<DateCost>>
        get() = _dateCost

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getLiveDateCostResult()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getLiveDateCostResult() {
        _dateCost = timeMasterRepository.getLiveDateCost()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun getActiveAttendeeCost(): List<DateCost> {
        val list = mutableListOf<DateCost>()
        val dating =
            UserManager.myDate.value?.filter { myDate ->
                myDate.active == true
            }?.map { myDate2 ->
                myDate2.name
            }

        if (dating != null) {
            for (d in dating.indices) {
                val item = dateCost.value?.filter { dateCost ->
                    dateCost.attendeeName == dating[d]
                }
                if (!item.isNullOrEmpty()) {
                    list.addAll(item)
                }
            }
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getLabels(): MutableList<String> {
        val cal = Calendar.getInstance().timeInMillis
        return mutableListOf(
            TimeUtil.stampToDateNoYear(cal - DAY_TIME_IN_MILLIS * 3, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal - DAY_TIME_IN_MILLIS * 2, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal - DAY_TIME_IN_MILLIS, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal, Locale.TAIWAN)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun countHistoryPrice(dateCost: List<DateCost>): Long {
        val cal = Calendar.getInstance().timeInMillis
        var daySum: Long = 0

        val allMoney = dateCost.filter {
            it.time!! > cal - DAY_TIME_IN_MILLIS * 3
        }.map {
            it.costPrice
        }
        Log.e("ALLMONEY", allMoney.toString())
        for (mon in allMoney) {
            if (mon != null) {
                daySum += mon
            }
        }

        return daySum
    }

    fun filterDayPrice() {

    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun countDayPrice(dateCost: List<DateCost>, historyPrice: Long): MutableList<Long> {
        var sumPrice = historyPrice
        var dayMoney = listOf<Long?>()
        val dateDayPrice = mutableListOf<Long>()
        for (l in getLabels().indices) {

            dayMoney = dateCost.filter {
                TimeUtil.stampToDateNoYear(it.time ?: 0, Locale.TAIWAN) == getLabels()[l]
            }.map {
                it.costPrice
            }
            filterDayPrice = dayMoney
            Log.e("ALLMONEY111", filterDayPrice.toString())

            for (money in dayMoney.indices) {

                sumPrice += dayMoney[money]!!
            }
            dateDayPrice.add(sumPrice)
        }
        return dateDayPrice
    }

}
