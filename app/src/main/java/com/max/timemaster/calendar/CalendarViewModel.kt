package com.max.timemaster.calendar


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    var returnDate: String?,
    var selectAttendeeArg: String?
) : ViewModel() {
    private val _selectEvent = MutableLiveData<List<CalendarEvent>>()

    val selectEvent: LiveData<List<CalendarEvent>>
        get() = _selectEvent

    var liveAllEvent = MutableLiveData<List<CalendarEvent>>()



    var selectAttendee = MutableLiveData<String>().apply {
        selectAttendeeArg?.let { value = it }
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

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    val selectDate = MutableLiveData<String>()

    var greaterThan: Long = 0
    var lessThan: Long = 0


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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
