package com.max.timemaster.profile.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.max.timemaster.*
import com.max.timemaster.databinding.DialogProfileEditBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.dateToStamp
import java.util.*


class ProfileEditDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<ProfileEditViewModel> {
        getVmFactory(
            ProfileEditDialogArgs.fromBundle(
                requireArguments()
            ).selectedDateKey
        )
    }

    lateinit var binding: DialogProfileEditBinding

    private companion object {
        const val PHOTO_FROM_GALLERY = 0
    }

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

        binding.imageView.setOnClickListener {
            toAlbum()
        }

        binding.btnActive.setOnClickListener {
            if (viewModel.edActive.value != true) {

                findNavController().navigate(
                    NavigationDirections.navigateToMessengerDialog(
                        MessageType.ACTIVE.value
                    )
                )

                viewModel.edActive.value = true
            } else {

                findNavController().navigate(
                    NavigationDirections.navigateToMessengerDialog(
                        MessageType.ARCHIVE.value)
                )
                viewModel.edActive.value = false
            }
        }

        binding.editBirthday.setOnClickListener {
            datePicker()
        }

        binding.buttonPublish.setOnClickListener {
            viewModel.updateDate()
        }

        val adapter = ProfileColorEditAdapter(viewModel)
        binding.recyclerProfileColor.adapter = adapter
        adapter.submitList(viewModel.getColorList())

        viewModel.edColor.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {

                binding.imageView.background.setTint(Color.parseColor("#$it"))
                binding.editBirthday.background.setTint(Color.parseColor("#$it"))
                binding.editBirthday.setTextColor(Color.parseColor("#$it"))
            }
        })
        viewModel.leave.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {

                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })

//        viewModel.imagePhoto.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            it?.let {
//                bindProfileImage(binding.imageView, it)
//            }
//        })

        permission()
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
            viewModel.editDate.value =
                dateToStamp("$year-${newMonth.toInt() + 1}-$newDay", Locale.TAIWAN)
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

    private fun toAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY)
    }

    private fun permission() {
        val permissionList = arrayListOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        var size = permissionList.size
        var number = 0
        while (number < size) {
            if (ActivityCompat.checkSelfPermission(
                    TimeMasterApplication.instance.applicationContext,
                    permissionList[number]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.removeAt(number)
                number -= 1
                size -= 1
            }
            number += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions(
            (activity as AppCompatActivity),
            permissionList.toArray(array),
            PHOTO_FROM_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO_FROM_GALLERY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.data?.let { viewModel.syncImage(it) }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
        }
    }
}
