package com.max.timemaster.calendar

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.MainViewModel
import com.max.timemaster.NavigationDirections
import com.max.timemaster.R
import com.max.timemaster.data.CalendarEvent
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
            CalendarFragmentArgs.fromBundle(requireArguments()).returnDate,
            CalendarFragmentArgs.fromBundle(requireArguments()).selectAttendee
        )
    }

    lateinit var widget: MaterialCalendarView
    lateinit var binding: FragmentCalendarBinding
    lateinit var mainViewModel: MainViewModel
    var previousDates = mutableListOf<Any?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.selectDate.value = LocalDate.now().toString()

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
                mark()
                mainViewModel.liveMyDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

//                    adapter.submitList(viewModel.selectEvent.value)
                    UserManager.selectTime.value = viewModel.selectEvent.value

                    mainViewModel.selectAttendee.value?.let { select ->
                        if (select.isEmpty()) {
                            //驅除封存
//                            val date = UserManager.myDate.value?.filter { myDate ->
//                                myDate.active == true
//                            }?.map {
//                                it.name
//                            }
//                            val list = mutableListOf<CalendarEvent>()
//
//                            if (date != null) {
//                                for (x in date.indices){
//                                    val item = UserManager.selectTime.value?.filter { dateEvent ->
//                                        dateEvent.attendee == date[x]
//                                    }
//                                    if (!item.isNullOrEmpty()){
//                                        list.add(item[0])}
//                                }
//                            }

                            adapter.submitList(UserManager.selectTime.value)
                        } else {

                            val x = UserManager.selectTime.value?.filter {date ->
                                date.attendee == select
                            }
                            Log.d("viewModel22", "${UserManager.selectTime.value}")
                            Log.d("viewModel2", "$x")

                            adapter.submitList(x)
                        }
                    }


                })
            }
        })


        mainViewModel.selectAttendee.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { select ->
                mark()
                if (select.isEmpty()) {

                    adapter.submitList(UserManager.selectTime.value)

                    binding.btnAdd.visibility = View.GONE
                } else {

                    val selectedPersonEvents = UserManager.selectTime.value?.filter {
                        it.attendee == select
                    }
                    adapter.submitList(selectedPersonEvents)

                    binding.btnAdd.visibility = View.VISIBLE
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

        UserManager.allEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                mark()
                showSelectEvent()
            }
        })




        if (viewModel.returnDate == null) {
            widget.setSelectedDate(calendar)
        } else {
            val selectedDate = viewModel.returnDate!!.split("-")
            val year = selectedDate[0]
            val month = selectedDate[1]
            val date = selectedDate[2]
            widget.selectedDate = CalendarDay.from(year.toInt(), month.toInt(), date.toInt())
        }
        if (mainViewModel.selectAttendee.value.isNullOrEmpty()){
            showTodayEvent()
        }else{
            showSelectEvent()
            Log.d("XXXX","${viewModel.selectEvent.value}")
        }

    }


    private fun mark() {
        val allEventTime = UserManager.allEvent.value?.map {
            it.dateStamp
        }
        val selectDateMark = UserManager.allEvent.value?.let { allEvent ->
            allEvent.filter { att ->
                att.attendee == mainViewModel.selectAttendee.value
            }.map {
                it.dateStamp
            }
        }
        val list = mutableListOf<String>()

        widget.removeDecorators()
        widget.invalidateDecorators()


        if (mainViewModel.selectAttendee.value?.isEmpty()!!) {

            if (allEventTime != null) {
                for (a in allEventTime) {
                    val selectedDate = stampToDate(a as Long, Locale.TAIWAN).split("-")
                    val year = selectedDate[0]
                    val month = selectedDate[1]
                    val date = selectedDate[2]
                    val calendarDay = CalendarDay.from(
                        year.toInt(),
                        month.toInt(),
                        date.toInt()
                    ) // year, month, date
                    widget.addDecorators(
                        CurrentDayDecorator(
                            resources.getColor(R.color.black),
                            calendarDay
                        )
                    )

                    list.add(stampToDate(a, Locale.TAIWAN))

                    previousDates.add(calendarDay)
                }
            }
            Log.e("Max", previousDates.toString())
        } else {

            if (selectDateMark != null) {
                for (a in selectDateMark) {
                    val selectedDate = stampToDate(a as Long, Locale.TAIWAN).split("-")
                    val year = selectedDate[0]
                    val month = selectedDate[1]
                    val date = selectedDate[2]
                    val calendarDay = CalendarDay.from(
                        year.toInt(),
                        month.toInt(),
                        date.toInt()
                    ) // year, month, date
                    widget.addDecorators(
                        CurrentDayDecorator(
                            resources.getColor(R.color.black),
                            calendarDay
                        )
                    )

                    list.add(stampToDate(a, Locale.TAIWAN))

                    previousDates.add(calendarDay)
                }
            }
        }
    }

    // 顯示選擇日期的Event
    private fun showSelectEvent() {
        widget.setOnDateChangedListener { widget, date, selected ->
            viewModel.selectDate.value = date.date.toString()
            viewModel.greaterThan = dateToStampTime(date.date.toString() + " 00:00", Locale.TAIWAN)
            viewModel.lessThan = dateToStampTime(date.date.toString() + " 23:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()
        }
    }

    private fun showTodayEvent() {
        if (viewModel.returnDate == null) {
            viewModel.greaterThan =
                dateToStampTime(LocalDate.now().toString() + " 00:00", Locale.TAIWAN)
            viewModel.lessThan =
                dateToStampTime(LocalDate.now().toString() + " 23:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()
        } else {
            viewModel.greaterThan = dateToStampTime(viewModel.returnDate + " 00:00", Locale.TAIWAN)
            viewModel.lessThan = dateToStampTime(viewModel.returnDate + " 23:59", Locale.TAIWAN)
            viewModel.getSelectEventResult()

        }
    }
//    fun remove(){
//
//        if(previousDates.isNotEmpty()){
//            for(day in previousDates)
//            widget.removeDecorator(CurrentDayDecorator(
//                resources.getColor(R.color.black_3f3a3a),
//                day
//            ))
//
//        }
//        widget.invalidateDecorators()
//        widget.removeDecorators()
//    }

    override fun onResume() {
        super.onResume()
        mark()
    }
}





