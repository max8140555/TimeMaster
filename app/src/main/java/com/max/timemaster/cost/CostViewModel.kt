package com.max.timemaster.cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.timemaster.data.CostContent
import com.max.timemaster.data.TimeMasterRepository

class CostViewModel(timeMasterRepository: TimeMasterRepository) : ViewModel() {
    var fakerCost = MutableLiveData<List<CostContent>>().apply {
        value = listOf(
            CostContent(
                costTitle = "口紅",
                costPrice = 800
            ),
            CostContent(
                costTitle = "外套",
                costPrice = 3600
            ),
            CostContent(
                costTitle = "手錶",
                costPrice = 4800
            ),
            CostContent(
                costTitle = "外套",
                costPrice = 3600
            ),
            CostContent(
                costTitle = "外套",
                costPrice = 3600
            ),
            CostContent(
                costTitle = "外套",
                costPrice = 3600
            ),CostContent(
                costTitle = "外套",
                costPrice = 3600
            ),CostContent(
                costTitle = "外套",
                costPrice = 3600
            )

        )
    }
}
