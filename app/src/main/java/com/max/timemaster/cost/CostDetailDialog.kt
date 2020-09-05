package com.max.timemaster.cost


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.*
import com.max.timemaster.databinding.DialogCostDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil
import org.threeten.bp.LocalDate
import java.lang.String
import java.util.*


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

        binding.layoutPublish.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )

        binding.selectDate.setOnClickListener {
            datePicker()
        }

        binding.buttonPublish.setOnClickListener {
            if (!viewModel.edTitle.value.isNullOrEmpty() && !viewModel.edMoney.value.isNullOrEmpty()) {

                viewModel.uploadCost()

            } else {

                findNavController().navigate(NavigationDirections.navigateToMessengerDialog(
                    MessageType.INCOMPLETE_TEXT.value))
            }
        }

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer {
            it?.let { attendee ->
                viewModel.edAttendee.value = attendee
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
            binding.selectDate.text = "$year-${newMonth.toInt() + 1}-$newDay"
            viewModel.edTime.value = "$year-${newMonth.toInt() + 1}-$newDay"
        }

        val date = TimeUtil.splitDateSet(LocalDate.now().toString(), "-")

        activity?.let {
            DatePickerDialog(it, dateListener, date.year, date.month-1, date.day).show()
        }
    }

}
