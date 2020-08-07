package com.max.timemaster.calendar


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil
import com.max.timemaster.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*

class CalendarViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    var returnDate: String?,
    var selectAttendeeArg: String?
) : ViewModel() {

    private var greaterThan: Long = 0
    private var lessThan: Long = 0

    var selectAttendee = MutableLiveData<String>().apply {
        selectAttendeeArg?.let { value = it }
    }

    private val _selectEvent = MutableLiveData<List<CalendarEvent>>()

    val selectEvent: LiveData<List<CalendarEvent>>
        get() = _selectEvent

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

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    val selectDate = MutableLiveData<String>().apply {
        value = LocalDate.now().toString()
    }

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        showTodayEvent()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getActiveAttendeeEvents(): List<CalendarEvent> {
        val list = mutableListOf<CalendarEvent>()
        val date = UserManager.myDate.value?.filter { myDate ->
            myDate.active == true
        }?.map {
            it.name
        }

        if (date != null) {
            for (d in date.indices) {
                val item = UserManager.selectEvent.value?.filter { dateEvent ->
                    dateEvent.attendee == date[d]
                }
                if (!item.isNullOrEmpty()) {
                    list.addAll(item)
                }
            }
        }
        return list
    }

    fun getActiveDateTime(): MutableList<Long?> {
        val activeDateMark = mutableListOf<Long?>()
        val activeDate = UserManager.myDate.value?.filter { myDate ->
            myDate.active == true
        }?.map {
            it.name
        }
        activeDate?.let {
            for (name in activeDate) {
                val event = UserManager.allEvent.value?.filter {
                    it.attendee == name
                }?.map {
                    it.dateStamp
                }
                if (event != null) {
                    for (i in event) {
                        activeDateMark.add(i)
                    }
                }
            }
        }
        return activeDateMark
    }

    fun getSelectAttendeeTime(selectAttendee: String): MutableList<Long?> {
        return UserManager.allEvent.value?.let { allEvent ->
            allEvent.filter { att ->
                att.attendee == selectAttendee
            }.map {
                it.dateStamp
            }
        }?.toMutableList() ?: mutableListOf()
    }

    fun setTimeInterval(selectDate: String?) {
        greaterThan =
            TimeUtil.dateToStampTime(
                selectDate + TimeMasterApplication.instance.getString(R.string.space_text) + TimeMasterApplication.instance.getString(
                    R.string.time_0000_text
                ),
                Locale.TAIWAN
            )
        lessThan =
            TimeUtil.dateToStampTime(
                selectDate + TimeMasterApplication.instance.getString(R.string.space_text) + TimeMasterApplication.instance.getString(
                    R.string.time_2359_text
                ),
                Locale.TAIWAN
            )

    }

    private fun showTodayEvent() {
        when (returnDate) {
            null -> {
                setTimeInterval(LocalDate.now().toString())
            }
            else -> {
                setTimeInterval(returnDate)
            }
        }
        getSelectEventResult()
    }


    fun getSelectEventResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = timeMasterRepository.getSelectEvent(greaterThan, lessThan)

            _selectEvent.value = when (result) {
                is com.max.timemaster.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is com.max.timemaster.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.max.timemaster.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value =
                        TimeMasterApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

}
