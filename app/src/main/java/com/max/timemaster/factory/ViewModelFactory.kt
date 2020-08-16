package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.MainViewModel
import com.max.timemaster.cost.CostDetailDialogViewModel
import com.max.timemaster.cost.CostViewModel
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.favorite.FavoriteDetailDialogViewModel
import com.max.timemaster.favorite.FavoriteViewModel
import com.max.timemaster.login.LoginViewModel
import com.max.timemaster.profile.ProfileViewModel
import com.max.timemaster.profile.detail.ProfileDetailViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val timeMasterRepository: TimeMasterRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(timeMasterRepository)
                isAssignableFrom(FavoriteViewModel::class.java) ->
                    FavoriteViewModel(timeMasterRepository)
                isAssignableFrom(FavoriteDetailDialogViewModel::class.java) ->
                    FavoriteDetailDialogViewModel(timeMasterRepository)
                isAssignableFrom(CostViewModel::class.java) ->
                    CostViewModel(timeMasterRepository)
                isAssignableFrom(CostDetailDialogViewModel::class.java) ->
                    CostDetailDialogViewModel(timeMasterRepository)
                isAssignableFrom(ProfileDetailViewModel::class.java) ->
                    ProfileDetailViewModel(timeMasterRepository)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(timeMasterRepository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(timeMasterRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
