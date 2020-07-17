package com.max.timemaster.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.MainViewModel

import com.max.timemaster.R
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.DialogFavoriteDetailBinding
import com.max.timemaster.ext.getVmFactory

class FavoriteDetailDialog : AppCompatDialogFragment() {

    private val viewModel by viewModels<FavoriteDetailDialogViewModel> {
        getVmFactory()
    }

    lateinit var binding: DialogFavoriteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = DialogFavoriteDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        var dataSet = mutableListOf("","興趣","飲料","包包")
        binding.niceSpinner.attachDataSource(dataSet)
        binding.buttonPublish.setOnClickListener {

            viewModel.postAddDateFavorite(addDateFavorite())
        }

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.edAttendee.value = it
            }
        })




        return binding.root
    }

    fun addDateFavorite():DateFavorite{
        return DateFavorite(
            viewModel.edAttendee.value,
            viewModel.edTitle.value,
            viewModel.edContent.value
        )
    }

}
