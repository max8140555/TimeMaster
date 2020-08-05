package com.max.timemaster.ext

import androidx.fragment.app.Fragment
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.MyDate
import com.max.timemaster.factory.*

import com.max.timemaster.profile.ProfileTypeFilter


fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TimeMasterApplication).timeMasterRepository
    return ViewModelFactory(repository)
}
fun Fragment.getVmFactory(selectDate: String): CalendarDetailViewModelFactory {
    val repository = (requireContext().applicationContext as TimeMasterApplication).timeMasterRepository
    return CalendarDetailViewModelFactory(repository, selectDate)
}
fun Fragment.getVmFactory(selectDate: String?, selectAttendee: String?): CalendarViewModelFactory {
    val repository = (requireContext().applicationContext as TimeMasterApplication).timeMasterRepository
    return CalendarViewModelFactory(repository, selectDate,selectAttendee)
}

fun Fragment.getVmFactory(catalogType: ProfileTypeFilter): ProfileItemViewModelFactory {
    val repository = (requireContext().applicationContext as TimeMasterApplication).timeMasterRepository
    return ProfileItemViewModelFactory(repository, catalogType)
}
fun Fragment.getVmFactory(myDate: MyDate): ProfileEditViewModelFactory {
    val repository = (requireContext().applicationContext as TimeMasterApplication).timeMasterRepository
    return ProfileEditViewModelFactory(repository, myDate)
}
