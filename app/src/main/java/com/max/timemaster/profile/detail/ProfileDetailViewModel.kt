package com.max.timemaster.profile.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.MyDate
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil.dateToStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class ProfileDetailViewModel(private val timeMasterRepository: TimeMasterRepository) : ViewModel() {
    var editDate = MutableLiveData<String>()
    var edDateName = MutableLiveData<String>()
    var myDate = MutableLiveData<MyDate>()
    var edColor = MutableLiveData<String>()
    var selectedPosition =MutableLiveData<Int>().apply {
        value = -1
    }

    var imagePhoto =MutableLiveData<String>().apply {
        value = "https://scontent.ftpe7-3.fna.fbcdn.net/v/t1.0-9/s960x960/35431412_2043916532349506_2460415922964267008_o.jpg?_nc_cat=102&_nc_sid=85a577&_nc_ohc=EBuFzo9IZ3IAX_pIYrJ&_nc_ht=scontent.ftpe7-3.fna&_nc_tp=7&oh=c395614d7ae115058ef3928720f2112a&oe=5F38D943"
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

   fun addDate(image: String?) {
       myDate.value = edDateName.value?.let {
           MyDate(
               name = it,
               birthday = editDate.value?.let { it1 -> dateToStamp(it1, Locale.TAIWAN) },
               color = edColor.value,
               position = selectedPosition.value,
               image = image

           )
       }
   }
    fun postAddDate(myDate: MyDate) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = timeMasterRepository.postDate(myDate)) {
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
}
