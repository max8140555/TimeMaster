package com.max.timemaster.cost

import android.icu.util.Calendar
import android.os.Build
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


class CostViewModel(private val timeMasterRepository: TimeMasterRepository) : ViewModel() {

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
    fun getLabels(): MutableList<String>{
        val cal = Calendar.getInstance().timeInMillis
        return mutableListOf(
            TimeUtil.stampToDateNoYear(cal - 86400000 * 3, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal - 86400000 * 2, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal - 86400000, Locale.TAIWAN)
            , TimeUtil.stampToDateNoYear(cal, Locale.TAIWAN)
        )
    }
}
