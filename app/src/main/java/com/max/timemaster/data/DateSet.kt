package com.max.timemaster.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateSet(
    var year: Int = -1,
    var month: Int = -1,
    var day: Int = -1
): Parcelable