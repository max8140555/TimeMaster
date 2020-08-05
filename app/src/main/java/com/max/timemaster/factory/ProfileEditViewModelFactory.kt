package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.data.MyDate
import com.max.timemaster.data.TimeMasterRepository
import com.max.timemaster.profile.edit.ProfileEditViewModel


@Suppress("UNCHECKED_CAST")
class ProfileEditViewModelFactory(
    private val timeMasterRepository: TimeMasterRepository,
    private val myDate: MyDate
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ProfileEditViewModel::class.java) ->
                    ProfileEditViewModel(timeMasterRepository, myDate)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}