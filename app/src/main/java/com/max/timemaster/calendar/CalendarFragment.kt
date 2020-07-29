package com.max.timemaster.calendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.max.timemaster.MainViewModel
import com.max.timemaster.NavigationDirections
import com.max.timemaster.R
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.databinding.FragmentCalendarBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.CurrentDayDecorator
import com.max.timemaster.util.Logger
import com.max.timemaster.util.TimeUtil.dateToStampTime
import com.max.timemaster.util.TimeUtil.stampToDate
import com.max.timemaster.util.UserManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_calendar.*
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
                UserManager.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    mark()
                })

                mainViewModel.liveMyDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer { listDate ->

                    if (listDate.isNullOrEmpty()){
                        binding.prompt.visibility = View.VISIBLE
                        binding.imagePrompt.visibility = View.VISIBLE
                        binding.imagePrompt.setImageResource(R.drawable.icon_profile)
                    }else{
                        if (viewModel.selectEvent.value.isNullOrEmpty()){
                            binding.prompt.visibility = View.VISIBLE
                            binding.imagePrompt.visibility = View.VISIBLE
                            binding.prompt.text = "點左上角按鈕，選擇對象新增行程吧！"
                            binding.imagePrompt.setImageResource(R.drawable.toolbar_menu)
                        }else{
                            binding.prompt.visibility = View.GONE
                            binding.imagePrompt.visibility =View.GONE
                        }


                    }
//                    adapter.submitList(viewModel.selectEvent.value)
                    UserManager.selectTime.value = viewModel.selectEvent.value

                    mainViewModel.selectAttendee.value?.let { select ->
                        if (select.isEmpty()) {
                            // 驅除封存
                            val date = UserManager.myDate.value?.filter { myDate ->
                                myDate.active == true
                            }?.map {
                                it.name
                            }
                            val list = mutableListOf<CalendarEvent>()

                            if (date != null) {
                                for (x in date.indices) {
                                    val item = UserManager.selectTime.value?.filter { dateEvent ->
                                        dateEvent.attendee == date[x]
                                    }
                                    if (!item.isNullOrEmpty()) {
                                        list.addAll(item)
                                    }
                                }
                            }

                            adapter.submitList(list.sortedBy { it.dateStamp })
                        } else {

                            val x = UserManager.selectTime.value?.filter { date ->
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
                UserManager.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    mark()
                })
                if (select.isEmpty()) {
                    val date = UserManager.myDate.value?.filter { myDate ->
                        myDate.active == true
                    }?.map {
                        it.name
                    }
                    val list = mutableListOf<CalendarEvent>()

                    if (date != null) {
                        for (x in date.indices) {
                            val item = UserManager.selectTime.value?.filter { dateEvent ->
                                dateEvent.attendee == date[x]
                            }
                            if (!item.isNullOrEmpty()) {
                                list.addAll(item)
                            }
                        }
                    }
                    adapter.submitList(list.sortedBy { it.dateStamp })

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
        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let{
                if (viewModel.status.value == LoadApiStatus.LOADING){
                    binding.lottieLoading.visibility = View.VISIBLE
                }else{
                    Handler().postDelayed({
                        binding.lottieLoading.visibility = View.GONE
                    },1000)
                }
            }
        })



        return binding.root
    }

    override fun onStart() {
        super.onStart()


        // Identify BottomSheetBehavior to present different calendar layout

        bottomSheetBehavior = BottomSheetBehavior.from<NestedScrollView>(nested_view)

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        Handler().postDelayed({changeToMonth()},200)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        })


        val calendar = LocalDate.now()


        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView

//        viewModel.getAllEventTimeResult()

        UserManager.allEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                UserManager.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    mark()
                })
                showSelectEvent()
            }
        })




        if (viewModel.returnDate == null) {
            widget.setSelectedDate(calendar)
        } else {
            val selectedDate = viewModel.returnDate!!.split("-")
            val year = selectedDate[0]
            val month = selectedDate[1]
            val day = selectedDate[2]
            widget.selectedDate = CalendarDay.from(year.toInt(), month.toInt(), day.toInt())
        }

        showTodayEvent()


    }


    private fun mark() {

        val date = UserManager.myDate.value?.filter { myDate ->
            myDate.active == true
        }?.map {
            it.name
        }

        val listAllEvent = mutableListOf<Long?>()
        Log.d("XXXXXXX", "$date")
        date?.let {

            for (x in date) {
                val event = UserManager.allEvent.value?.filter {
                    it.attendee == x
                }?.map {
                    it.dateStamp
                }
                if (event != null) {
                    for (i in event) {
                        listAllEvent.add(i)
                    }
                }

            }
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

            for (a in listAllEvent) {
                val selectedDate = stampToDate(a as Long, Locale.TAIWAN).split("-")
                val year = selectedDate[0]
                val month = selectedDate[1]
                val day = selectedDate[2]
                val calendarDay = CalendarDay.from(
                    year.toInt(),
                    month.toInt(),
                    day.toInt()
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

    override fun onResume() {
        super.onResume()
        UserManager.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mark()
        })
    }

}





