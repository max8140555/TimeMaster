package com.max.timemaster


import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.TimeUtil
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
        val date: String = DateFormat.format("MM-dd", cal).toString()
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

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            )
            .into(imgView)
    }
}

@BindingAdapter("profileImageUrl")
fun bindProfileImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .circleCrop()
            )
            .into(imgView)
    }
}

/**
 * Displays exp to [TextView] by [Long]
 */

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("exp")
fun bindExp(textView: TextView, exp: Long?) {
    exp?.let {
        textView.text = "${exp % 100}%"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("level")
fun bindLevel(textView: TextView, exp: Long?) {
    exp?.let {
        textView.text = "${exp / 100 + 1}"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("title")
fun bindTitle(textView: TextView, exp: Long?) {
    exp?.let {
        when {
            exp / 100 + 1 >= 3 -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level3_6)
            }
            exp / 100 + 1 >= 7 -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level7_up)
            }
            else -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level1_2)
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("loginTime")
fun bindLoginTime(textView: TextView, timestamp: Long?) {
    timestamp?.let {
        val cal = Calendar.getInstance(Locale.TAIWAN).timeInMillis

        val day = (cal - it) / 86400000 +1

        textView.text = day.toString()
    }
}

