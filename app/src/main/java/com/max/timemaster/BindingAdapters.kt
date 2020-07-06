package com.max.timemaster


import android.os.Build
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.max.timemaster.network.LoadApiStatus
import java.util.*

/**
 * Displays date to [TextView] by [Long]
 */

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("timestamp")
fun bindDate(textView: TextView, timestamp: Long?) {
    timestamp?.let {
        val cal = Calendar.getInstance(Locale.TAIWAN)
        cal.timeInMillis = it
        val date: String = DateFormat.format("yyyy-MM-dd", cal).toString()
        textView.text = date
    }
}

/**
 * According to [LoadApiStatus] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
    }
}