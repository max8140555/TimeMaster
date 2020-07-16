package com.max.timemaster.cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.DateCost

import com.max.timemaster.data.TimeMasterRepository

class CostViewModel(timeMasterRepository: TimeMasterRepository) : ViewModel() {
    var fakerCost = MutableLiveData<List<DateCost>>().apply {
        value = listOf(
            DateCost(
                costTitle = "口紅",
                costPrice = 800
            ),
            DateCost(
                costTitle = "外套",
                costPrice = 3600
            ),
            DateCost(
                costTitle = "手錶",
                costPrice = 4800
            ),
            DateCost(
                costTitle = "外套",
                costPrice = 3600
            ),
            DateCost(
                costTitle = "外套",
                costPrice = 3600
            ),
            DateCost(
                costTitle = "外套",
                costPrice = 3600
            ),DateCost(
                costTitle = "外套",
                costPrice = 3600
            ),DateCost(
                costTitle = "外套",
                costPrice = 3600
            )

        )
    }
}
