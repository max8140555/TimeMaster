package com.max.timemaster.calendar


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil.dateToStampTime
import com.max.timemaster.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class CalendarDetailViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    val selectDate: String
) : ViewModel() {

    var setData = MutableLiveData<CalendarEvent>()
    var editTitle = MutableLiveData<String>()
    var editAttendee = MutableLiveData<String>()
    var editContent = MutableLiveData<String>()
    var editDate = MutableLiveData<String>()
    var editTime = MutableLiveData<String>().apply {
        value = TimeMasterApplication.instance.getString(R.string.time_0000_text)
    }

    var editEndTime = MutableLiveData<String>().apply {
        value = TimeMasterApplication.instance.getString(R.string.time_0000_text)
    }

    val isConflict = MutableLiveData<Boolean>().apply {
        value = null
    }

    private var _inputState = MutableLiveData<Int>()

    val inputState: LiveData<Int>
        get() = _inputState

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

    init {
        editDate.value = selectDate
    }

    fun checkInputData() {

        val stampStart = dateToStampTime(
            "${editDate.value} ${editTime.value}",
            Locale.TAIWAN
        ) + 1000

        val stampEnd = dateToStampTime(
            "${editDate.value} ${editEndTime.value}",
            Locale.TAIWAN
        ) + 1000

        when {
            stampStart >= stampEnd -> {
                _inputState.value = 0
            }
            editTitle.value.isNullOrEmpty() -> {
                _inputState.value = 1
            }

            else -> {
                insertCalendar(stampStart, stampEnd)
                checkIfConflict(stampStart, stampEnd)
            }
        }
    }

    //判斷是否行程時間衝突
    private fun checkIfConflict(
        stampStart: Long,
        stampEnd: Long
    ) {
        val start = UserManager.allEvent.value?.map { it.dateStamp }
        val end = UserManager.allEvent.value?.map { it.dateEndStamp }
        if (!start.isNullOrEmpty() && !end.isNullOrEmpty()) {

            loop@ for (x in start.indices) {
                for (y in end.indices) {
                    if (x == y) {
                        if (stampEnd <= start[x]!! || stampStart >= end[y]!!) {
                            if (x == start.size - 1) {
                                isConflict.value = false
                            }
                        } else {
                            isConflict.value = true
                            break@loop
                        }
                    }
                }
            }
        } else {
            isConflict.value = false
        }
    }

    fun uploadEvent() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = setData.value?.let { timeMasterRepository.postEvent(it) }) {
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
                    _error.value =
                        TimeMasterApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    private fun insertCalendar(stamp: Long, stampEnd: Long) {

        setData.value = CalendarEvent(
            editTitle.value,
            editAttendee.value,
            editContent.value,
            stamp,
            stampEnd
        )
    }

    fun nothing() {

    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun restoreInputState() {
        _inputState.value = null
    }

    fun onLeft() {
        _leave.value = null
    }
}

