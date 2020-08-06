package com.max.timemaster.calendar


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
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
import com.max.timemaster.util.TimeUtil.splitDateSet
import java.lang.String.format
import java.util.*

private const val SELECT_START_TIME = 0
private const val SELECT_END_TIME = 1

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

        binding.selectDate.setOnClickListener {
            datePicker()
        }
        binding.selectTime.setOnClickListener {
            timePicker(SELECT_START_TIME)
        }
        binding.selectEndTime.setOnClickListener {
            timePicker(SELECT_END_TIME)
        }

        binding.save.setOnClickListener {

            viewModel.checkInputData()

            viewModel.inputState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    when (it) {
                        0 -> {
                            findNavController().navigate(
                                NavigationDirections.navigateToMessengerDialog(
                                    MessageType.TIME_ERROR.value
                                )
                            )
                            viewModel.restoreInputState()
                        }

                        1 -> {
                            findNavController().navigate(
                                NavigationDirections.navigateToMessengerDialog(
                                    MessageType.NOT_TITLE.value
                                )
                            )
                            viewModel.restoreInputState()
                        }

                        else -> viewModel.nothing()
                    }
                }
            })

        }

        binding.btnAllDay.setOnCheckedChangeListener { buttonView, _ ->
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

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                viewModel.editAttendee.value = it
            }
        })

        viewModel.isConflict.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { isConflict ->

                if (isConflict) {

                    findNavController().navigate(
                        NavigationDirections.navigateToMessengerDialog(
                            MessageType.CONFLICT.value
                        )
                    )
                } else {
                    viewModel.postEvent()
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

    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun timePicker(number: Int) {
        val calendar = Calendar.getInstance()
        val calendarHour = calendar.get(Calendar.HOUR_OF_DAY)
        val calendarMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(activity, { _, hour, minute ->
            val newHour = format("%02d", hour)
            val newMinute = format("%02d", minute)
            if (number == SELECT_START_TIME) {
                binding.selectTime.text = "$newHour:$newMinute"
                viewModel.editTime.value = "$newHour:$newMinute"
            } else {
                binding.selectEndTime.text = "$newHour:$newMinute"
                viewModel.editEndTime.value = "$newHour:$newMinute"
            }
        }, calendarHour, calendarMinute, true).show()
    }


    @SuppressLint("DefaultLocale", "SetTextI18n")
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

        val date = splitDateSet(viewModel.selectDate,"-")

        activity?.let {
            DatePickerDialog(it, dateListener, date.year, date.month-1, date.day).show()
        }
    }

}
