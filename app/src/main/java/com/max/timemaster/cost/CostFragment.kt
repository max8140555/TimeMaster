package com.max.timemaster.cost

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.max.timemaster.MainViewModel
import com.max.timemaster.R

import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.FragmentCostBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.stampToDateNoYear
import com.max.timemaster.util.UserManager
import java.util.*
import kotlin.collections.ArrayList

private const val NOT_ATTENDEE = 0
private const val SELECT_ATTENDEE = 1
private const val PROMPT_GONE = 2
private const val DAY_TIME_IN_MILLIS = 86400000

class CostFragment : Fragment() {

    private val viewModel by viewModels<CostViewModel> {
        getVmFactory()
    }

    lateinit var binding: FragmentCostBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding = FragmentCostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_costDetailDialog)
        }

        val adapter = CostAdapter()
        binding.recyclerCost.adapter = adapter

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer { attendee ->
            attendee?.let {
                viewModel.dateCost.observe(viewLifecycleOwner, Observer { dataCosts ->
                    dataCosts?.let { dateCost ->

                        UserManager.dateCost.value = dataCosts
                        val dateSelect = dateCost.filter { date ->
                            date.attendeeName == attendee
                        }

                        if (attendee.isEmpty()) {
                            mainViewModel.liveMyDate.observe(viewLifecycleOwner,
                                Observer { listDate ->

                                    if (listDate.isNullOrEmpty()) {
                                        checkHaveDatePrompt(NOT_ATTENDEE)
                                    } else {
                                        if (viewModel.getActiveAttendeeCost().isNullOrEmpty()) {
                                            checkHaveDatePrompt(SELECT_ATTENDEE)
                                        } else {
                                            checkHaveDatePrompt(PROMPT_GONE)
                                        }
                                    }
                                })

                            setPluralData(viewModel.getActiveAttendeeCost())
                            adapter.submitList(
                                viewModel.getActiveAttendeeCost().sortedByDescending {
                                    it.time
                                })
                            binding.btnAdd.visibility = GONE

                        } else {

                            promptVisibility(dateSelect)
                            setPluralData(dateSelect)
                            adapter.submitList(dateSelect)
                            binding.btnAdd.visibility = VISIBLE
                        }
                    }
                })
            }
        })

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setPluralData(allListDateCost: List<DateCost>) {

        val dates = UserManager.myDate.value?.filter {
            it.active == true
        }?.map {
            it.name
        }
        val dataSetGroup = mutableListOf<LineDataSet>()

        dates?.let { dates ->


            for (d in dates.indices) {

                  var daySum: Long = 0
                var dayMoney = listOf<Long?>()
                val dateDayPrice = mutableListOf<Long>()

                val dateCost = allListDateCost.filter {
                    it.attendeeName == dates[d]
                }

                val cal = Calendar.getInstance().timeInMillis
                val allMoney = dateCost.filter {
                    it.time!! < cal - 86400000 * 3
                }.map {
                    it.costPrice
                }

                for (mon in allMoney) {
                    if (mon != null) {
                        daySum += mon
                    }
                }

                for (l in viewModel.getLabels().indices) {

                    dayMoney = dateCost.filter {
                        stampToDateNoYear(it.time ?: 0, Locale.TAIWAN) == viewModel.getLabels()[l]
                    }.map {
                        it.costPrice
                    }
                    Log.e("Max111", "$dayMoney")


                    for (money in dayMoney.indices) {

                        daySum += dayMoney[money]!!
                    }


                    Log.e("Max", "list = ${dates[d]} , ${viewModel.getLabels()[l]}, $dayMoney")
                    dateDayPrice.add(daySum)

                    Log.e("Max", "dayMoney = $dateDayPrice")


                }

                val entries: MutableList<Entry> = ArrayList()

                for (x in 0..dateDayPrice.size) {
                    for (y in dateDayPrice.indices) {
                        if (x == y) {
                            entries.add(Entry(x.toFloat(), dateDayPrice[x].toFloat()))
                        }
                    }
                }


                val dateName = UserManager.myDate.value?.filter {
                    it.name == dates[d]
                }?.map { it.name }
                val dateColor = UserManager.myDate.value?.filter {
                    it.name == dates[d]
                }?.map {
                    it.color
                }
                val dataSet = LineDataSet(entries, dateName?.get(0))
                dataSet.color = Color.parseColor("#${dateColor?.get(0)}")
                dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
                dataSet.valueTextSize = 12F
                dataSet.getEntriesForXValue(0F)

                if (!dayMoney.isNullOrEmpty() || daySum  != 0L) {
                    dataSetGroup.add(dataSet)
                }
            }
        }

        val lineChart = binding.lineChartView
        // Controlling X axis
        val xAxis = lineChart.xAxis
        xAxis?.let {
            it.valueFormatter = IndexAxisValueFormatter(viewModel.getLabels())
            it.position = XAxis.XAxisPosition.BOTTOM
            it.textSize = 12f
            it.setDrawLabels(true)
            it.setDrawGridLines(false)
            it.mAxisMaximum = 5f
            it.position = XAxis.XAxisPosition.BOTTOM
        }

        // Controlling right side of y axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        // Controlling left side of y axis
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.setDrawLabels(false)
        yAxisLeft.setDrawGridLines(false)

        // Setting Data
        val data = LineData(dataSetGroup as List<ILineDataSet>?)

        lineChart.let {
            it.description.text = "時間"
            it.description.textSize = 10F
            it.data = data
            it.setTouchEnabled(true)
            it.axisLeft.setStartAtZero(true)
            it.invalidate()
            it.notifyDataSetChanged()
        }

    }


    private fun promptVisibility(cost: List<DateCost>?) {
        if (cost.isNullOrEmpty()) {

            binding.prompt.text = getString(R.string.hint_cost_text)
            binding.imagePrompt.visibility = VISIBLE
            binding.prompt.visibility = VISIBLE

            binding.imagePrompt.setOnClickListener {
                findNavController().navigate(R.id.navigate_to_costDetailDialog)
            }
        } else {

            binding.imagePrompt.visibility = GONE
            binding.prompt.visibility = GONE

        }
    }

    private fun checkHaveDatePrompt(state: Int?) {
        when (state) {
            NOT_ATTENDEE -> {
                binding.prompt.visibility = VISIBLE
                binding.prompt.text = getString(R.string.hint_add_date_text)
            }
            SELECT_ATTENDEE -> {
                binding.prompt.visibility = VISIBLE
                binding.prompt.text = getString(R.string.hint_select_date_text)
                binding.imagePrompt.visibility = GONE
            }
            PROMPT_GONE -> {
                binding.imagePrompt.visibility = GONE
                binding.prompt.visibility = GONE
            }
        }
    }
}



