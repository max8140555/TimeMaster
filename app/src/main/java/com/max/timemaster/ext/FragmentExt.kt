package com.max.timemaster.ext

import androidx.fragment.app.Fragment
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.factory.CalendarDetailViewModelFactory
import com.max.timemaster.factory.CalendarViewModelFactory

import com.max.timemaster.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
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
//
//fun Fragment.getVmFactory(user: User?): ProfileViewModelFactory {
//    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
//    return ProfileViewModelFactory(repository, user)
//}
//
//fun Fragment.getVmFactory(product: Product): ProductViewModelFactory {
//    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
//    return ProductViewModelFactory(repository, product)
//}
//
//fun Fragment.getVmFactory(orderNumber: String): OrderNumberViewModelFactory {
//    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
//    return OrderNumberViewModelFactory(repository, orderNumber)
//}
////fun Fragment.getVmFactory(reviewsAllOrder: ReviewsAllOrder): RecordViewModelFactory {
////    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
////    return RecordViewModelFactory(repository,reviewsAllOrder)
////}
//
//fun Fragment.getVmFactory(catalogType: CatalogTypeFilter): CatalogItemViewModelFactory {
//    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
//    return CatalogItemViewModelFactory(repository, catalogType)
//}
//
//fun Fragment.getVmFactory(reviews: Reviews, numberKey: String): EvaluationDialogViewModelFactory {
//    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
//    return EvaluationDialogViewModelFactory(repository, reviews, numberKey)
//}