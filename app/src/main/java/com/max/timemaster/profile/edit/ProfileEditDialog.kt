package com.max.timemaster.profile.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.max.timemaster.R
import com.max.timemaster.databinding.DialogProfileDetailBinding
import com.max.timemaster.databinding.DialogProfileEditBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.profile.detail.ProfileColorAdapter
import com.max.timemaster.profile.detail.ProfileColorEditAdapter
import com.max.timemaster.profile.detail.ProfileDetailViewModel
import com.max.timemaster.profile.detail.ProfileItemAdapter
import com.max.timemaster.util.UserManager
import java.util.*

class ProfileEditDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<ProfileEditViewModel> { getVmFactory() }
    lateinit var binding: DialogProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.layoutPublish.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )
        val adapter = ProfileColorEditAdapter(viewModel)
        binding.recyclerProfileColor.adapter = adapter
        val arrayList = this.resources.getStringArray(R.array.colorList)
        val colorList = mutableListOf<String>()
        for (x in 0..9) {
            colorList.add(arrayList[x])
        }
        adapter.submitList(colorList)




        binding.editBirthday.setOnClickListener {
            datePicker()
        }
        binding.buttonPublish.setOnClickListener {
            viewModel.addDate()
            viewModel.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    viewModel.myDate.value?.let { it1 -> viewModel.postAddDate(it1) }
                    UserManager.addDate.value = viewModel.edDateName.value
                    Log.d(" UserManager.addDate.value", "${UserManager.addDate.value}")
                }
            })
        }

        viewModel.edColor.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
//                binding.editBirthday.background.colorFilter = Color.parseColor("#$it"
                binding.editBirthday.background.setTint(Color.parseColor("#$it"))
                binding.editBirthday.setTextColor(Color.parseColor("#$it"))
            }
        })


        return binding.root
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun datePicker() {
        val calender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calender.set(year, month, day)
            String.format("yyyy-MM-dd")
            val newMonth = String.format("%02d", month)
            val newDay = String.format("%02d", day)
            binding.editBirthday.text = "$year-${newMonth.toInt() + 1}-$newDay"
            viewModel.editDate.value = "$year-${newMonth.toInt() + 1}-$newDay"
        }

        context?.let {
            DatePickerDialog(
                it,
                dateListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

}
