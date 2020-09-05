package com.max.timemaster.cost


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.DateCost
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil.dateToStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class CostDetailDialogViewModel( private val timeMasterRepository: TimeMasterRepository) : ViewModel() {

    var edAttendee = MutableLiveData<String>()
    var edTitle = MutableLiveData<String>()
    var edMoney = MutableLiveData<String>()
    var edContent = MutableLiveData<String>()
    var edTime = MutableLiveData<String>().apply {
        value = LocalDate.now().toString()
    }
    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun uploadCost() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.postCost(generateDateCost())) {
                is com.max.timemaster.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
                }
                is com.max.timemaster.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is com.max.timemaster.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = TimeMasterApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    private fun generateDateCost(): DateCost {
        return DateCost(
            edAttendee.value,
            edTitle.value,
            edMoney.value?.toLong(),
            edContent.value,
            dateToStamp(edTime.value?:"${LocalDate.now()}", Locale.TAIWAN)
        )
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }

    fun nothing() {

    }
}
