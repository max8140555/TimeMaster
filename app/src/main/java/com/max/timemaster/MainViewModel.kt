package com.max.timemaster

import android.os.UserManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.MyDate
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.data.User
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.ServiceLocator.timeMasterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val timeMasterRepository: TimeMasterRepository
) : ViewModel() {

    var liveUser = MutableLiveData<User>()
    var liveMyDate = MutableLiveData<List<MyDate>>()
    var liveAllEvent = MutableLiveData<List<CalendarEvent>>()

    var selectAttendee = MutableLiveData<String>()

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



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }












    fun postUser(user: User) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.postUser(user)) {
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

    fun getLiveUserResult() {
        liveUser = timeMasterRepository.getLiveUser()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }
    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }
    fun getLiveMyDateResult() {
        liveMyDate = timeMasterRepository.getLiveMyDate()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun getAllEventResult() {
        liveAllEvent = timeMasterRepository.getLiveAllEvent()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }
}
