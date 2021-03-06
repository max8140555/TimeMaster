package com.max.timemaster.profile.edit


import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.MyDate
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileEditViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    private val selectDate: MyDate
) : ViewModel() {

    var editDate = MutableLiveData<Long>().apply {
        value = selectDate.birthday
    }
    var edDateName = MutableLiveData<String>().apply {
        value = selectDate.name
    }
    var edColor = MutableLiveData<String>().apply {
        value = selectDate.color
    }
    var selectedPosition = MutableLiveData<Int>().apply {
        value = selectDate.position
    }
    var imagePhoto = MutableLiveData<String>().apply {
        value = selectDate.image
    }
    var edActive = MutableLiveData<Boolean>().apply {
        value = selectDate.active
    }

    private var _saveImage = MutableLiveData<String>()

    val saveImage: LiveData<String>
        get() = _saveImage

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

    private fun addDate(): MyDate? {
        return edDateName.value?.let {
            MyDate(
                name = it,
                birthday = editDate.value,
                color = edColor.value,
                active = edActive.value,
                position = selectedPosition.value,
                image = imagePhoto.value
            )
        }
    }

    fun updateDate() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = addDate()?.let { timeMasterRepository.updateDate(it) }) {
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

    fun syncImage(uri: Uri) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.syncImage(uri)) {
                is com.max.timemaster.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    imagePhoto.value = result.data

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

    fun getColorList(): MutableList<String>{
        val arrayList = TimeMasterApplication.instance.resources.getStringArray(R.array.colorList)
        val colorList = mutableListOf<String>()
        for (x in 0..9) {
            colorList.add(arrayList[x])
        }
        return colorList
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
