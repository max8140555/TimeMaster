package com.max.timemaster


import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
 * Uses the Glide library to load an image by URL into an [ImageView] (circleCrop)
 */
@BindingAdapter("profileImageUrl")
fun bindProfileImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
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
@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("exp")
fun bindExp(textView: TextView, exp: Long?) {
    exp?.let {
        textView.text = "${it % 100}%"
    }
}

/**
 * Use [Long] to convert exp to level and display as [TextView]
 */
@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("level")
fun bindLevel(textView: TextView, exp: Long?) {
    exp?.let {
        textView.text = "${it / 100 + 1}"
    }
}

/**
 * Use [Long] to convert exp to level and display the corresponding Title as [TextView]
 */
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("title")
fun bindTitle(textView: TextView, exp: Long?) {
    exp?.let {
        when {
            it / 100 + 1 in 3..6 -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level3_6)
            }
            it / 100 + 1 >= 7 -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level7_up)
                textView.textSize = 12F
            }
            else -> {
                textView.text = TimeMasterApplication.instance.getString(R.string.level1_2)
            }
        }
    }
}

/**
 * Displays login days to [TextView] by [Long]
 */
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("loginTime")
fun bindLoginTime(textView: TextView, timestamp: Long?) {
    timestamp?.let {
        val cal = Calendar.getInstance(Locale.TAIWAN).timeInMillis
        val day = (cal - it) / 86400000 + 1

        textView.text = day.toString()
    }
}