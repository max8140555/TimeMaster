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
//        binding.viewModel = viewModel
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_costDetailDialog)
        }


        viewModel.getLiveDateCostResult()
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

                            // All date

                            val dating =
                                com.max.timemaster.util.UserManager.myDate.value?.filter { myDate ->
                                    myDate.active == true
                                }?.map { myDate2 ->
                                    myDate2.name
                                }

                            val allDateCostList = mutableListOf<DateCost>()

                            if (dating != null) {
                                for (x in dating.indices) {
                                    val item = viewModel.dateCost.value?.filter { dateCost ->
                                        dateCost.attendeeName == dating[x]
                                    }
                                    if (!item.isNullOrEmpty()) {
                                        allDateCostList.addAll(item)
                                    }
                                }
                            }

                            mainViewModel.liveMyDate.observe(
                                viewLifecycleOwner,
                                Observer { listDate ->
                                    if (listDate.isNullOrEmpty()) {
                                        binding.prompt.visibility = VISIBLE
                                        binding.prompt.text = getString(R.string.hint_add_date_text)

                                    } else {
                                        if (allDateCostList.isNullOrEmpty()) {

                                            binding.prompt.text = getString(R.string.hint_select_date_text)
                                            binding.prompt.visibility = VISIBLE
                                        } else {
                                            binding.imagePrompt.visibility = GONE
                                            binding.prompt.visibility = GONE
                                        }
                                    }
                                })


                            setPluralData(allDateCostList)
                            adapter.submitList(allDateCostList.sortedByDescending {
                                it.time
                            })
                            binding.btnAdd.visibility = GONE

                        } else {

                            // Selected Date
                            if (dateSelect.isNullOrEmpty()) {
                                binding.imagePrompt.setImageResource(R.drawable.icon_add)
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

        val lineChart = binding.lineChartView

        val cal = Calendar.getInstance().timeInMillis
        Log.e("Max", "$cal")

        val labels: MutableList<String> = mutableListOf(
            stampToDateNoYear(cal - 86400000 * 3, Locale.TAIWAN)
            , stampToDateNoYear(cal - 86400000 * 2, Locale.TAIWAN)
            , stampToDateNoYear(cal - 86400000, Locale.TAIWAN)
            , stampToDateNoYear(cal, Locale.TAIWAN)
        )


        lineChart.description.text = "時間"
        lineChart.description.textSize = 10F
        lineChart.xAxis.apply {
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.textSize = 12f
            lineChart.xAxis.setDrawLabels(true)
            lineChart.xAxis.setDrawGridLines(false)
        }

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
                val dateName = UserManager.myDate.value?.filter {
                    it.name == dates[d]
                }?.map {
                    it.name
                }
                val dateColor = UserManager.myDate.value?.filter {
                    it.name == dates[d]
                }?.map {
                    it.color
                }

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

                for (l in labels.indices) {

                    dayMoney = dateCost.filter {
                        stampToDateNoYear(it.time ?: 0, Locale.TAIWAN) == labels[l]
                    }.map {
                        it.costPrice
                    }
                    Log.e("Max111", "$dayMoney")


                    for (money in dayMoney.indices) {

                        daySum += dayMoney[money]!!
                    }


                    Log.e("Max", "list = ${dates[d]} , ${labels[l]}, $dayMoney")
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
                val dataSet = LineDataSet(entries, dateName?.get(0))
                dataSet.color = Color.parseColor("#${dateColor?.get(0)}")
                dataSet.valueTextColor =
                    ContextCompat.getColor(requireContext(), R.color.black)
                dataSet.valueTextSize = 12F
                dataSet.getEntriesForXValue(0F)

                if (!dayMoney.isNullOrEmpty() || daySum != 0L) {
                    dataSetGroup.add(dataSet)
                }

                Log.e("Max", "dayMonet = ${dataSetGroup.size}")
            }

        }

        // Controlling X axis
        val xAxis = lineChart.xAxis
        xAxis.mAxisMaximum = 5f
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Controlling right side of y axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        //***
        // Controlling left side of y axis
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.setDrawLabels(false)
        yAxisLeft.setDrawGridLines(false)


//        // Setting Data
        val data = LineData(dataSetGroup as List<ILineDataSet>?)
        lineChart.data = data
        lineChart.setTouchEnabled(true)
        lineChart.axisLeft.setStartAtZero(true)
        lineChart.invalidate()
        lineChart.notifyDataSetChanged()
    }
}



