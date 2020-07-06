package com.max.timemaster.calendar

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.google.firebase.firestore.FirebaseFirestore
import com.max.timemaster.R

import com.max.timemaster.databinding.FragmentCalendarBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.Logger.d


import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.threeten.bp.LocalDate
import java.util.*
import java.util.logging.Logger


class CalendarFragment : Fragment() {
    private val viewModel by viewModels<CalendarViewModel> { getVmFactory() }
    lateinit var widget: MaterialCalendarView
    lateinit var binding : FragmentCalendarBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        viewModel.getArticlesResult()
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_global_calendarDetailFragment)
        }
        val adapter = CalendarAdapter()
        binding.recyclerCalendar.adapter = adapter

        viewModel.calendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })




        return binding.root
    }

    override fun onStart() {
        super.onStart()
        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView
        widget.setOnDateChangedListener { widget, date, selected ->
            Toast.makeText(
                activity,
                "current date \n ${date.date}",
                Toast.LENGTH_SHORT
            ).show()

        }
        val calendar = LocalDate.now()
        widget.setSelectedDate(calendar)
    }

    fun addData() {
        val calendar = FirebaseFirestore.getInstance().collection("calendar")
        val document = calendar.document()
        val data = hashMapOf(
            "calendarTitle" to "約會",
            "attendee" to "小小",
            "createdTime" to Calendar.getInstance().timeInMillis,
            "calendarContent" to "吃西餐廳",
            "hour" to 8,
            "minute" to 20
        )
        document.set(data)
    }

}




