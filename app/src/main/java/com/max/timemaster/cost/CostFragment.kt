package com.max.timemaster.cost

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

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
        binding = FragmentCostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_costDetailDialog)
        }




        return binding.root
    }



}
