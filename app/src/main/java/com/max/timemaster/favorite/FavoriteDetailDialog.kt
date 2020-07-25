package com.max.timemaster.favorite

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.max.timemaster.MainViewModel
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.DialogFavoriteDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.UserManager
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
        binding.layoutPublish.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))


        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer {selectDate ->

            viewModel.edAttendee.value = selectDate

            val listTitle = UserManager.dateFavorite.value?.filter {
                it.attendeeName == selectDate
            }?.map {
                it.favoriteTitle
            }?.toMutableList()

            listTitle?.add(0,"")
            binding.niceSpinner.attachDataSource(listTitle)
            binding.niceSpinner.setOnItemSelectedListener( object:
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    listTitle?.let {
                        viewModel.edTitle.value = it[position]
                    }

                }

            })
        })

        chipGroup = binding.chipGroup


        binding.addChip.setOnClickListener {

            val chip = Chip(chipGroup.context)
            val content = viewModel.edContent.value
            val listContent = viewModel.edListContent
            if (content != null) {
                viewModel.edListContent.add(content)
                for (i in listContent.indices) {
                    val textContent = listContent[i]
                    //移除chip
                    chip.setOnClickListener {
                        chip.isCloseIconEnabled = !chip.isCloseIconEnabled

                        //Added click listener on close icon to remove tag from ChipGroup
                        chip.setOnCloseIconClickListener {
                            viewModel.edListContent.remove(textContent)
                            chipGroup.removeView(chip)
                        }
                    }
//                    val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
//                    val chipColors = intArrayOf(Color.parseColor("#E8DDB5"))
//                    val chipColorsStateList = ColorStateList(states, chipColors)
                    val color = UserManager.myDate.value?.filter {
                        it.name == viewModel.edAttendee.value
                    }!![0].color
                    val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
                    val colors = intArrayOf(Color.parseColor("#$color"))
                    val colorsStateList = ColorStateList(states, colors)
                    chip.chipBackgroundColor = colorsStateList
                    val paddingDp = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        TimeMasterApplication.instance.resources.displayMetrics
                    ).toInt()

                    chip.setPadding(40, paddingDp, paddingDp, paddingDp)
                }
                chip.text = content
                chip.setTextColor(Color.BLACK)

                chipGroup.addView(chip)
            }


            viewModel.edContent.value = null
        }





        binding.buttonPublish.setOnClickListener {
            viewModel.postAddDateFavorite(addDateFavorite())

        }




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
