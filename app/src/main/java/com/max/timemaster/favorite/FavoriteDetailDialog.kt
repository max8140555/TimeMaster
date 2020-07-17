package com.max.timemaster.favorite

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.max.timemaster.MainViewModel
import com.max.timemaster.R
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.DialogFavoriteDetailBinding
import com.max.timemaster.ext.getVmFactory
import kotlinx.android.synthetic.main.dialog_favorite_detail.*

class FavoriteDetailDialog : AppCompatDialogFragment() {

    private val viewModel by viewModels<FavoriteDetailDialogViewModel> {
        getVmFactory()
    }
    lateinit var binding: DialogFavoriteDetailBinding
    lateinit var chipGroup: ChipGroup
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
        var dataSet = mutableListOf("", "興趣", "飲料", "包包")
        binding.niceSpinner.attachDataSource(dataSet)
        chipGroup = binding.chipGroup
        binding.addChip.setOnClickListener {

            val genres =
                arrayOf("Thriller", "Comedy", "Adventure")
            for (genre in genres) {
                val chip = Chip(chipGroup.context)
                chip.text = genre


                chip.setTextColor(Color.BLACK)
                chipGroup.addView(chip)
            }
        }





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

    fun addDateFavorite(): DateFavorite {
        return DateFavorite(
            viewModel.edAttendee.value,
            viewModel.edTitle.value,
            viewModel.edContent.value
        )
    }

}
