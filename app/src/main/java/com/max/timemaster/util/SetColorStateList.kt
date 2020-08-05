package com.max.timemaster.util

import android.content.res.ColorStateList
import android.graphics.Color

object SetColorStateList {

    fun setColorStateList(colorTint: String): ColorStateList {

        val color = if (colorTint.contains("#")){
            colorTint
        } else {
            "#${colorTint}"
        }
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
        val colors = intArrayOf(Color.parseColor(color))

        return ColorStateList(states, colors)
    }

}