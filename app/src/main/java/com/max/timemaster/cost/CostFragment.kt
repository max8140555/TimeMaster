package com.max.timemaster.cost

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.MainViewModel

import com.max.timemaster.R
import com.max.timemaster.databinding.FragmentCostBinding
import com.max.timemaster.ext.getVmFactory

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
                            adapter.submitList(it)
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


}
