package com.max.timemaster.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class CurrentDayDecorator(var color: Int, currentDay: CalendarDay) : DayViewDecorator {

    var myDay = currentDay

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color))
    }

}

