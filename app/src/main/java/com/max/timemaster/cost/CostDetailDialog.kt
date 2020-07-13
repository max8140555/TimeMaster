package com.max.timemaster.cost


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.max.timemaster.R
import com.max.timemaster.databinding.DialogCostDetailBinding
import com.max.timemaster.ext.getVmFactory


class CostDetailDialog : AppCompatDialogFragment() {

    private val viewModel by viewModels<CostDetailDialogViewModel> {
        getVmFactory(
        )
    }

    lateinit var binding: DialogCostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCostDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        return binding.root

    }
}