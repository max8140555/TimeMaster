package com.max.timemaster.profile.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.profile.ProfileTypeFilter
import com.max.timemaster.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CatalogItemFragment].
 */
class ProfileItemViewModel(
    private val timeMasterRepository:TimeMasterRepository,
    private val profileType: ProfileTypeFilter // Handle the type for each catalog item, myDate: com.max.timemaster.data.MyDate){}, myDate: com.max.timemaster.data.MyDate){}
) : ViewModel() {

//    private val sourceFactory = PagingDataSourceFactory(catalogType)
//
//    val pagingDataProducts: LiveData<PagedList<Product>> = sourceFactory.toLiveData(6, null)

//    // Handle load api status
//    val status: LiveData<LoadApiStatus> = Transformations.switchMap(sourceFactory.sourceLiveData) {
//        it.statusInitialLoad
//    }
//
//    // Handle load api error
//    val error: LiveData<String> = Transformations.switchMap(sourceFactory.sourceLiveData) {
//        it.errorInitialLoad
//    }
//


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


    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

//    fun refresh() {
//        if (status.value != LoadApiStatus.LOADING) {
//            sourceFactory.sourceLiveData.value.invalidate()
//        }
//    }
}