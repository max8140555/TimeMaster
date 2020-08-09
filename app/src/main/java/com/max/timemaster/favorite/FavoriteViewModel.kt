package com.max.timemaster.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class FavoriteViewModel(private val timeMasterRepository: TimeMasterRepository) : ViewModel() {

    var dateFavorite = MutableLiveData<List<DateFavorite>>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus
    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getLiveDateCostResult()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getLiveDateCostResult() {
        dateFavorite = timeMasterRepository.getLiveDateFavorite()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun getActiveAttendeeFavorite(): List<DateFavorite> {
        val date = UserManager.myDate.value?.filter { myDate ->
            myDate.active == true
        }?.map {
            it.name
        }

        val list = mutableListOf<DateFavorite>()

        if (date != null) {
            for (x in date.indices) {
                val item = dateFavorite.value?.filter { dateFavorite ->
                        dateFavorite.attendeeName == date[x]
                    }
                if (!item.isNullOrEmpty()) {
                    list.addAll(item)
                }
            }
        }
        return list
    }

}
