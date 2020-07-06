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
import com.max.timemaster.NavigationDirections
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
        viewModel.selectDate.value = LocalDate.now().toString()

        binding.btnAdd.setOnClickListener {

            viewModel.selectDate.value?.let {selectDate ->
                findNavController().navigate(NavigationDirections.actionGlobalCalendarDetailFragment(selectDate))
            }


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
//            date.date.format(Da)


            viewModel.selectDate.value = date.date.toString()
            Log.d("viewModel.selectDate","${viewModel.selectDate.value}")
        }
        val calendar = LocalDate.now()
        widget.setSelectedDate(calendar)
    }



}




