package com.max.timemaster.favorite

import android.content.res.ColorStateList
import android.graphics.Color
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
            val chip = Chip(chipGroup.context)
            val content = viewModel.edContent.value
            val listContent = viewModel.edListContent
            if (content != null) {
                viewModel.edListContent.add(content)
            }
            for (i in listContent.indices) {
                val textContent = listContent[i]
                //移除chip
                chip.setOnClickListener {
                    chip.isCloseIconEnabled = !chip.isCloseIconEnabled

                    //Added click listener on close icon to remove tag from ChipGroup
                    chip.setOnCloseIconClickListener {
                        viewModel.edListContent.remove(textContent)
                        chipGroup.removeView(chip)
                        Log.e("123", "$listContent")
                    }
                }
                val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
                val chipColors = intArrayOf(Color.parseColor("#E8DDB5"))
                val chipColorsStateList = ColorStateList(states, chipColors)
                chip.chipBackgroundColor = chipColorsStateList

            }
            chip.text = content
            chip.setTextColor(Color.WHITE)
            chipGroup.addView(chip)
            Log.e("456", "$content")
            Log.e("456", "$listContent")
            viewModel.edContent.value = null
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
            viewModel.edListContent
        )
    }

}
