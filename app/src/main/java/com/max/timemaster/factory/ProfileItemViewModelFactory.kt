package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.profile.ProfileTypeFilter
import com.max.timemaster.profile.item.ProfileItemViewModel


@Suppress("UNCHECKED_CAST")
class ProfileItemViewModelFactory(
    private val timeMasterRepository: TimeMasterRepository,
    private val catalogType: ProfileTypeFilter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ProfileItemViewModel::class.java) ->
                    ProfileItemViewModel(
                        timeMasterRepository,
                        catalogType
                    )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}