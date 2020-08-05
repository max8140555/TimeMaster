package com.max.timemaster.calendar


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.*
import com.max.timemaster.databinding.FragmentCalendarDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil
import com.max.timemaster.util.TimeUtil.dateToStampTime
import com.max.timemaster.util.UserManager
import java.lang.String.format
import java.util.*

class CalendarDetailFragment : AppCompatDialogFragment() {
    private val viewModel by viewModels<CalendarDetailViewModel> {
        getVmFactory(
            CalendarDetailFragmentArgs.fromBundle(requireArguments()).datekey
        )
    }
    lateinit var binding: FragmentCalendarDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCalendarDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutPublish.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )
        viewModel.editDate.value = viewModel.selectDate

        binding.selectDate.setOnClickListener {
            datePicker()
        }
        binding.selectTime.setOnClickListener {
            selectTime(0)
        }
        binding.selectEndTime.setOnClickListener {
            selectTime(1)
        }
        binding.selectDate.text = viewModel.selectDate

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                viewModel.editAttendee.value = it
            }
        })

        binding.save.setOnClickListener {


           checkInputData()

        }

        binding.btnAllDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                viewModel.editTime.value = getString(R.string.time_0000_text)
                viewModel.editEndTime.value = getString(R.string.time_2359_text)
                binding.selectTime.isEnabled = false
                binding.selectEndTime.isEnabled = false
                binding.selectTime.setBackgroundResource(R.drawable.bg_publish)
                binding.selectEndTime.setBackgroundResource(R.drawable.bg_publish)

            } else {

                binding.selectTime.isEnabled = true
                binding.selectEndTime.isEnabled = true
                binding.selectTime.setBackgroundColor(Color.parseColor(getString(R.string.color_white_text)))
                binding.selectEndTime.setBackgroundColor(Color.parseColor(getString(R.string.color_white_text)))
            }
        }

        viewModel.isConflict.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { isConflict ->

                if (isConflict) {

                    findNavController().navigate(NavigationDirections.navigateToMessengerDialog(MessageType.CONFLICT.value))

                } else {
                    val stampStart = dateToStampTime(
                        "${viewModel.editDate.value} ${viewModel.editTime.value}",
                        Locale.TAIWAN
                    ) + 1000
                    val stampEnd = dateToStampTime(
                        "${viewModel.editDate.value} ${viewModel.editEndTime.value}",
                        Locale.TAIWAN
                    ) + 1000
                    val event = viewModel.insertCalendar(
                        stampStart,
                        stampEnd
                    )
                    viewModel.postEvent(event)
                }
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToCalendarFragment(
                        returnDate = viewModel.editDate.value as String
                    )
                )
                viewModel.onLeft()
            }
        })

        return binding.root
    }

    fun selectTime(number: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(activity, { _, hour, minute ->
            val newhour = format("%02d", hour)
            val newminute = format("%02d", minute)
            if (number == 0) {
                binding.selectTime.text = "$newhour:$newminute"
                viewModel.editTime.value = "$newhour:$newminute"
                Log.e("Max", "${viewModel.editTime.value}")
            } else {
                binding.selectEndTime.text = "$newhour:$newminute"
                viewModel.editEndTime.value = "$newhour:$newminute"
                Log.e("Max", "${viewModel.editEndTime.value}")
            }

        }, hour, minute, true).show()
    }


    fun datePicker() {
        val calender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calender.set(year, month, day)
            format("yyyy-MM-dd")
            val newMonth = format("%02d", month)
            val newDay = format("%02d", day)
            binding.selectDate.text = "$year-${newMonth.toInt() + 1}-$newDay"
            viewModel.editDate.value = "$year-${newMonth.toInt() + 1}-$newDay"
        }
        val selectedDate = viewModel.selectDate.split("-")
        val year = selectedDate[0]
        val month = selectedDate[1]
        val date = selectedDate[2]

        activity?.let {
            DatePickerDialog(
                it,
                dateListener,
                year.toInt(),
                month.toInt() - 1,
                date.toInt()
            ).show()
        }
    }

    fun checkInputData() {

        val start = UserManager.allEvent.value?.map { it.dateStamp }
        val end = UserManager.allEvent.value?.map { it.dateEndStamp }

        val stampStart = TimeUtil.dateToStampTime(
            "${viewModel.editDate.value} ${viewModel.editTime.value}",
            Locale.TAIWAN
        ) + 1000
        val stampEnd = TimeUtil.dateToStampTime(
            "${viewModel.editDate.value} ${viewModel.editEndTime.value}",
            Locale.TAIWAN
        ) + 1000

        when {
            stampStart >= stampEnd -> {

                findNavController().navigate(NavigationDirections.navigateToMessengerDialog(MessageType.TIME_ERROR.value))

            }
            viewModel.editTitle.value.isNullOrEmpty() -> {

                findNavController().navigate(NavigationDirections.navigateToMessengerDialog(MessageType.NOT_TITLE.value))
            }
            else -> viewModel.checkIfConflict(stampStart, stampEnd, start, end)
        }
    }
}
