package com.max.timemaster.calendar


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.MainViewModel
import com.max.timemaster.NavigationDirections
import com.max.timemaster.R
import com.max.timemaster.databinding.FragmentCalendarDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.dateToStampTime
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentCalendarDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutPublish.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))
        viewModel.editDate.value = viewModel.selectDate

        binding.selectDate.setOnClickListener {
            datePicker()
        }
        binding.selectTime.setOnClickListener {
            selectTime()
        }

        binding.selectDate.text = viewModel.selectDate

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                viewModel.editAttendee.value = it
            }
        })

        binding.save.setOnClickListener {
            val stamp = "${viewModel.editDate.value} ${viewModel.editTime.value}"
            val event = viewModel.insertCalendar(dateToStampTime(stamp, Locale.TAIWAN))
            viewModel.postEvent(event)
            findNavController().navigate(NavigationDirections.navigateToCalendarFragment(returnDate = viewModel.editDate.value as String))
            viewModel.onLeft()
        }



        return binding.root
    }

    fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(activity, { _, hour, minute ->
            val newhour = format("%02d", hour)
            val newminute = format("%02d", minute)
            binding.selectTime.text = "$newhour:$newminute"
            viewModel.editTime.value = "$newhour:$newminute"
        }, hour, minute, true).show()
    }


    fun datePicker() {
        val calender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calender.set(year, month, day)
            format("yyyy-MM-dd")
            var newMonth = format("%02d", month)
            var newDay = format("%02d", day)
            binding.selectDate.text = "$year-${newMonth.toInt()+1}-$newDay"
            viewModel.editDate.value = "$year-${newMonth.toInt()+1}-$newDay"
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


}
