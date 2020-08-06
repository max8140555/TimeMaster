package com.max.timemaster.calendar

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.max.timemaster.MainViewModel
import com.max.timemaster.NavigationDirections
import com.max.timemaster.R
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.DateSet
import com.max.timemaster.databinding.FragmentCalendarBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.util.CurrentDayDecorator
import com.max.timemaster.util.TimeUtil.splitDateSet
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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

                mainViewModel.liveMyDate.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer { listDate ->

                        if (listDate.isNullOrEmpty()) {
                            binding.prompt.visibility = View.VISIBLE
                            binding.prompt.text = getString(R.string.hint_add_date_text)

                        } else {
                            if (viewModel.selectEvent.value.isNullOrEmpty()) {
                                binding.prompt.visibility = View.VISIBLE
                                binding.prompt.text = getString(R.string.hint_select_date_text)
                            } else {
                                binding.prompt.visibility = View.GONE
                                binding.imagePrompt.visibility = View.GONE
                            }
                        }

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
                                        val item =
                                            UserManager.selectTime.value?.filter { dateEvent ->
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
                                if (x.isNullOrEmpty()) {
                                    binding.prompt.visibility = View.VISIBLE
                                    binding.imagePrompt.setImageResource(R.drawable.icon_add)
                                    binding.prompt.text = getString(R.string.hint_event_text)
                                    binding.imagePrompt.setOnClickListener {
                                        viewModel.selectDate.value?.let { selectDate ->
                                            findNavController().navigate(
                                                NavigationDirections.navigateToCalendarDetailFragment(
                                                    selectDate
                                                )
                                            )
                                        }
                                    }
                                }

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
                    if (list.isNullOrEmpty()) {
                        binding.prompt.visibility = View.VISIBLE
//                        binding.imagePrompt.visibility = View.VISIBLE
                        binding.prompt.text = getString(R.string.hint_select_date_text)
//                        binding.imagePrompt.setImageResource(R.drawable.toolbar_menu)
                    } else {
                        binding.prompt.visibility = View.GONE
                        binding.imagePrompt.visibility = View.GONE
                    }
                    adapter.submitList(list.sortedBy { it.dateStamp })

                    binding.btnAdd.visibility = View.GONE
                } else {

                    val selectedPersonEvents = UserManager.selectTime.value?.filter {
                        it.attendee == select
                    }
                    if (selectedPersonEvents.isNullOrEmpty()) {
                        binding.imagePrompt.visibility = View.VISIBLE
                        binding.imagePrompt.setImageResource(R.drawable.icon_add)
                        binding.prompt.visibility = View.VISIBLE
                        binding.prompt.text = getString(R.string.hint_event_text)
                        binding.imagePrompt.setOnClickListener {
                            viewModel.selectDate.value?.let { selectDate ->
                                findNavController().navigate(
                                    NavigationDirections.navigateToCalendarDetailFragment(
                                        selectDate
                                    )
                                )
                            }
                        }
                    }
                    adapter.submitList(selectedPersonEvents)

                    binding.btnAdd.visibility = View.VISIBLE
                }
            })


        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (viewModel.status.value == LoadApiStatus.LOADING) {
                    binding.lottieLoading.visibility = View.VISIBLE
                } else {
                    Handler().postDelayed({
                        binding.lottieLoading.visibility = View.GONE
                    }, 1000)
                }
            }
        })



        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bottomSheetBehavior = BottomSheetBehavior.from(nested_view)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
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
            viewModel.returnDate?.let {
                val date = splitDateSet(it,"-")
                widget.selectedDate = CalendarDay.from(date.year, date.month, date.day)
            }
        }
    }

    private fun mark() {
        widget.removeDecorators()
        widget.invalidateDecorators()

        if (mainViewModel.selectAttendee.value?.isEmpty()!!) {
            setMarkData(viewModel.getActiveDateTime())
        } else {
            setMarkData(viewModel.getSelectAttendeeTime(mainViewModel.selectAttendee.value ?: ""))
        }
    }

    // 顯示選擇日期的Event
    private fun showSelectEvent() {
        widget.setOnDateChangedListener { widget, date, selected ->
            viewModel.selectDate.value = date.date.toString()
            viewModel.setTimeInterval(date.date.toString())
            viewModel.getSelectEventResult()
        }
    }

    override fun onResume() {
        super.onResume()
        UserManager.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mark()
        })
    }

    private fun setMarkData(markTime: MutableList<Long?>) {
        for (mT in markTime) {
            mT?.let {

                val date = splitDateSet(stampToDate(mT, Locale.TAIWAN),"-")
                val calendarDay = CalendarDay.from(date.year, date.month, date.day)

                widget.addDecorators(
                    CurrentDayDecorator(
                        resources.getColor(R.color.black),
                        calendarDay
                    )
                )
            }
        }
    }
}





