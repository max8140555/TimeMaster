package com.max.timemaster.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.DateFavorite

import com.max.timemaster.data.MyDate
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class FavoriteViewModel(timeMasterRepository: TimeMasterRepository) : ViewModel() {

    var fakerFavorite = MutableLiveData<List<DateFavorite>>()

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

}
