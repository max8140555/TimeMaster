package com.max.timemaster.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.Logger
import com.max.timemaster.util.ServiceLocator.timeMasterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class CalendarDetailViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    val selectDate: String
) : ViewModel() {

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

    var editTitle = MutableLiveData<String>()
    var editAttendee = MutableLiveData<String>()
    var editDate = MutableLiveData<String>()
    var editTime = MutableLiveData<String>()
    var editContent = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

    fun postEvent(calendarEvent: CalendarEvent) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.postEvent(calendarEvent)) {
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
    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }



    fun insertCalendar(stamp: Long): CalendarEvent {
        return CalendarEvent(
            editTitle.value,
            editAttendee.value,
            editContent.value,
            stamp
        )
    }


//    fun addData() {
//        val calendar = FirebaseFirestore.getInstance().collection("calendar")
//        val document = calendar.document()
//        val data = hashMapOf(
//            "calendarTitle" to editTitle.value,
//            "attendee" to editAttendee.value,
//            "createdTime" to Calendar.getInstance().timeInMillis,
//            "calendarContent" to editContent.value,
//            "dataStamp" to 8
//        )
//        document.set(data)
//    }




}

