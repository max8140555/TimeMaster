package com.max.timemaster.cost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.max.timemaster.MainViewModel
import com.max.timemaster.R
import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.FragmentCostBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.stampToDateNoYear
import java.util.*
import kotlin.collections.ArrayList


class CostFragment : Fragment() {

    private val viewModel by viewModels<CostViewModel> {
        getVmFactory()
    }
    lateinit var binding: FragmentCostBinding
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
                viewModel.dateCost.observe(viewLifecycleOwner, Observer {
                    it?.let { dateCost ->
                        val dateSelect = dateCost.filter { date ->
                            date.attendeeName == attendee
                        }


                        if (attendee.isEmpty()) {

                            val date =
                                com.max.timemaster.util.UserManager.myDate.value?.filter { myDate ->
                                    myDate.active == true
                                }?.map {
                                    it.name
                                }

                            val list = mutableListOf<DateCost>()
                            val listMoney = mutableListOf<Long>()
                            val listTime = mutableListOf<String>()
                            if (date != null) {
                                for (x in date.indices) {
                                    val item = viewModel.dateCost.value?.filter { dateCost ->
                                        dateCost.attendeeName == date[x]
                                    }
                                    val money = item?.map { cost ->
                                        cost.costPrice
                                    }
                                    val time = item?.map { timedate ->
                                        timedate.time
                                    }

                                    if (!money.isNullOrEmpty()) {
                                        for (addMoney in money) {
                                            if (addMoney != null) {
                                                listMoney.add(addMoney)
                                            }
                                        }
                                        if (time != null) {
                                            for (addTime in time) {
                                                if (addTime != null) {
                                                    listTime.add(stampToDateNoYear(addTime, Locale.TAIWAN))
                                                }
                                            }
                                        }
                                        setData(listMoney,listTime)
                                    }


                                    Log.d("987", "$item")
                                    if (!item.isNullOrEmpty()) {
                                        list.addAll(item)
                                    }

                                }

                            }


                            Log.d("9987", "$list")
                            adapter.submitList(list)
                            binding.btnAdd.visibility = GONE
                        } else {
                            adapter.submitList(dateSelect)
                            binding.btnAdd.visibility = VISIBLE
                        }
                    }
                })
            }
        })




        return binding.root
    }

    fun setData(money: List<Long?>,labels: List<String>) {
        val entries: MutableList<Entry> = ArrayList()
//            viewModel.record.value?.fitDetail?.maxBy { it.weight }?.weight?.toFloat()?.let {
//                Entry(
//                    it, simpleDateFormat.format(viewModel.record.value!!.createdTime).toFloat())
//            }?.let { entries.add(it) }

//        val labels = arrayOf("","國文","數學","英文")
        binding.lineChartView.xAxis.apply {
            binding.lineChartView.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            binding.lineChartView.xAxis.labelCount = 3
            binding.lineChartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
            binding.lineChartView.xAxis.setDrawLabels(true)
            binding.lineChartView.xAxis.setDrawGridLines(false)
        }
        Log.d("money", "$money")

        for (x in 0..money.size) {
            for (y in money.indices) {
                if (x == y) {
                    entries.add(Entry(x.toFloat(), money[x]!!.toFloat()))
                }
            }
        }

//        for (x in floatList.indices) {
//            for (y in money.indices) {
//                if (x == y) {
//                    entries.add(Entry(floatList[x], money[x]!!.toFloat()))
//                }
//            }
//        }

//        entries.add(Entry(2f, 80F))
//        entries.add(Entry(3f, 9f))
//        entries.add(Entry(4f, 16f))
//        entries.add(Entry(5f, 25f))
//        entries.add(Entry(7f, 36f))
//        binding.lineChartView.
        binding.lineChartView.description.text = "時間"
        binding.lineChartView.description.textSize = 10F


        val dataSet = LineDataSet(entries, "$")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.A9A587)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
        //****
        // Controlling X axis
        val xAxis = binding.lineChartView.xAxis
        // Set the xAxis position to bottom. Default is top

        xAxis.position = XAxis.XAxisPosition.BOTTOM





        //Customizing x axis value
//        val months = arrayOf("M", "T", "W", "T", "F", "S", "S", "A", "A", "A")
//        val formatter = IAxisValueFormatter { value, axis -> months[value.toInt()] }
//        xAxis.granularity = 1f // minimum axis-step (interval) is 1
//
//
//            xAxis.valueFormatter = formatter as ValueFormatter?
        //***


        // Controlling right side of y axis
        val yAxisRight = binding.lineChartView.axisRight
        yAxisRight.isEnabled = false
        //***
        // Controlling left side of y axis
        val yAxisLeft = binding.lineChartView.axisLeft
        yAxisLeft.granularity = 1f

        // Setting Data
        val data = LineData(dataSet)
        binding.lineChartView.data = data
        binding.lineChartView.axisLeft.setStartAtZero(true)
        binding.lineChartView.invalidate()
        binding.lineChartView.notifyDataSetChanged()
        binding.lineChartView.setTouchEnabled(false)
    }

}
