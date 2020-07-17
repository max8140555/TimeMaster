package com.max.timemaster.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteDetailDialogViewModel(private var timeMasterRepository: TimeMasterRepository) :
    ViewModel() {

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


    var edAttendee = MutableLiveData<String>()
    var edTitle = MutableLiveData<String>()
    var edContent = MutableLiveData<String>()
    var edListContent = mutableListOf<String>()


    fun postAddDateFavorite(dateFavorite: DateFavorite) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.postFavorite(dateFavorite)) {
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

    private fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

}
