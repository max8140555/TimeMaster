package com.max.timemaster.calendar

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.max.timemaster.NavigationDirections
import com.max.timemaster.R
import com.max.timemaster.databinding.FragmentCalendarBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.CurrentDayDecorator
import com.max.timemaster.util.TimeUtil.dateToStampTime
import com.max.timemaster.util.TimeUtil.stampToDate
import com.max.timemaster.util.UserManager

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.threeten.bp.LocalDate
import java.util.*


class CalendarFragment : Fragment() {
    private val viewModel by viewModels<CalendarViewModel> {
        getVmFactory(
            CalendarFragmentArgs.fromBundle(
                requireArguments()
            ).returnDate
        )
    }
    lateinit var widget: MaterialCalendarView
    lateinit var binding: FragmentCalendarBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel





        viewModel.selectDate.value = LocalDate.now().toString()
        showTodayEvent()







        binding.btnAdd.setOnClickListener {
            viewModel.selectDate.value?.let { selectDate ->
                findNavController().navigate(
                    NavigationDirections.navigateToCalendarDetailFragment(
                        selectDate
                    )
                )
            }
        }


        val adapter = CalendarAdapter()
        binding.recyclerCalendar.adapter = adapter

        viewModel.selectEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        // LiveData .. 取得所有Event
        /*
         viewModel.getAllEventResult()
         viewModel.liveAllEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
             it?.let {
                 Log.d("liveAllEvent","${viewModel.liveAllEvent.value}")
             }
         })
         */


        return binding.root
    }

    override fun onStart() {
        super.onStart()


        val calendar = LocalDate.now()

        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView

        viewModel.getAllEventTimeResult()

        viewModel.liveAllEventTime.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                addAllEventMark()
            }
        })
        showSelectEvent()


        if (viewModel.returnDate == null) {
            widget.setSelectedDate(calendar)
        } else {
            val selectedDate = viewModel.returnDate!!.split("-")
            val year = selectedDate[0]
            val month = selectedDate[1]
            val date = selectedDate[2]
            widget.selectedDate = CalendarDay.from(year.toInt(), month.toInt(), date.toInt())
        }

    }


    // 把所有Event標示在calendar上
    private fun addAllEventMark() {
       var allEventTime = UserManager.allEvent.value?.map {
           it.dateStamp
       }
            val list = mutableListOf<String>()
            for (a in allEventTime!!) {
                val selectedDate = stampToDate(a as Long, Locale.TAIWAN).split("-")
                val year = selectedDate[0]
                val month = selectedDate[1]
                val date = selectedDate[2]
                val addDate =
                    CalendarDay.from(year.toInt(), month.toInt(), date.toInt()) // year, month, date
                widget.addDecorators(
                    CurrentDayDecorator(
                        resources.getColor(R.color.black_3f3a3a),
                        addDate
                    )
                )
                list.add(stampToDate(a , Locale.TAIWAN))
            }


    }

    // 顯示選擇日期的Event
    private fun showSelectEvent() {
        widget.setOnDateChangedListener { widget, date, selected ->
            viewModel.selectDate.value = date.date.toString()
            viewModel.greaterThan = dateToStampTime(date.date.toString() + " 00:00", Locale.TAIWAN)
            viewModel.lessThan = dateToStampTime(date.date.toString() + " 24:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()
        }
    }

    private fun showTodayEvent() {
        if (viewModel.returnDate == null) {
            viewModel.greaterThan =
                dateToStampTime(LocalDate.now().toString() + " 00:00", Locale.TAIWAN)
            viewModel.lessThan = dateToStampTime(LocalDate.now().toString() + " 24:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()
        } else {
            viewModel.greaterThan = dateToStampTime(viewModel.returnDate + " 00:00", Locale.TAIWAN)
            viewModel.lessThan = dateToStampTime(viewModel.returnDate + " 24:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()

        }
    }
}




