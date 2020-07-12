package com.max.timemaster.profile.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.max.timemaster.R
import com.max.timemaster.databinding.DialogProfileDetailBinding
import com.max.timemaster.ext.getVmFactory
import java.lang.String
import java.util.*

class ProfileDetailDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<ProfileDetailViewModel> { getVmFactory() }
    lateinit var binding: DialogProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.editBirthday.setOnClickListener {
            datePicker()
        }
        binding.buttonPublish.setOnClickListener {
            viewModel.addDate()
            viewModel.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    viewModel.myDate.value?.let { it1 -> viewModel.postAddDate(it1) }
                }
            })
        }




        return binding.root
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun datePicker() {
        val calender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calender.set(year, month, day)
            String.format("yyyy-MM-dd")
            var newMonth = String.format("%02d", month)
            var newDay = String.format("%02d", day)
            binding.editBirthday.text = "$year-${newMonth.toInt()+1}-$newDay"
            viewModel.editDate.value = "$year-$newMonth-$newDay"
        }

        context?.let {
            DatePickerDialog(
                it,
                dateListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

}
