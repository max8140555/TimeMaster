package com.max.timemaster.calendar


import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider


import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.databinding.FragmentCalendarDelailBinding
import java.util.*

class CalendarDetailFragment : Fragment() {
    lateinit var binding : FragmentCalendarDelailBinding
    private lateinit var viewModel: CalendarDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarDelailBinding.inflate(inflater, container, false)


        binding.selectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(activity, {
                    _, hour, minute->
                binding.selectTime.text = "選擇的時間是 $hour:$minute"
            }, hour, minute, true).show()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
