package com.max.timemaster

import androidx.lifecycle.ViewModel
import com.max.timemaster.data.TimeMasterRepository

class MessageViewModel(
    private val timeMasterRepository: TimeMasterRepository,
    val messenger: String
) : ViewModel()

