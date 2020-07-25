package com.max.timemaster.cost


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.MainViewModel
import com.max.timemaster.R
import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.DialogCostDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.UserManager


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
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = DialogCostDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.layoutPublish.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))
        binding.buttonPublish.setOnClickListener {
            addCost()?.let { it1 -> viewModel.postAddCost(it1) }
            Log.d("addCost","${addCost()}")
        }

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.edAttendee.value = it
                Log.d("addCost", it)
            }
        })



        return binding.root

    }

    fun addCost(): DateCost? {
        return DateCost(
            viewModel.edAttendee.value,
                viewModel.edTitle.value,
                viewModel.edMoney.value?.toLong(),
                viewModel.edContent.value
            )

    }

}
