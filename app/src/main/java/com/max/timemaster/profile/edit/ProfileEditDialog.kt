package com.max.timemaster.profile.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage

import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.bindProfileImage
import com.max.timemaster.databinding.DialogProfileDetailBinding
import com.max.timemaster.databinding.DialogProfileEditBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.profile.detail.*
import com.max.timemaster.util.TimeUtil.dateToStamp
import com.max.timemaster.util.UserManager
import java.util.*

class ProfileEditDialog : AppCompatDialogFragment() {
    private val viewModel by viewModels<ProfileEditViewModel> { getVmFactory(ProfileEditDialogArgs.fromBundle(requireArguments()).selectedDateKey) }
    lateinit var binding: DialogProfileEditBinding
    var saveUri: Uri? = null
    private var imageUri = ""

    private companion object {
        val PHOTO_FROM_GALLERY = 0
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
        permission()

        binding.layoutPublish.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_slide_up
            )
        )
        binding.imageView.setOnClickListener {
            toAlbum()
        }
        val adapter = ProfileColorEditAdapter(viewModel)
        binding.recyclerProfileColor.adapter = adapter
        val arrayList = this.resources.getStringArray(R.array.colorList)
        val colorList = mutableListOf<String>()
        for (x in 0..9) {
            colorList.add(arrayList[x])
        }
        adapter.submitList(colorList)


        binding.btnActive.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked){
                viewModel.edActive.value = true
                Log.d("edActive", "${viewModel.edActive.value}")
            }else{
                viewModel.edActive.value = false
                Log.d("edActive", "${viewModel.edActive.value}")
            }
        }
        Log.e("Max","${binding.btnActive.isChecked}")
        binding.editBirthday.setOnClickListener {
            datePicker()
        }
        binding.buttonPublish.setOnClickListener {
            viewModel.addDate()
            viewModel.myDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    viewModel.myDate.value?.let { it1 -> viewModel.updateDate(it1) }
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
            viewModel.editDate.value = dateToStamp("$year-${newMonth.toInt() + 1}-$newDay", Locale.TAIWAN)
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
    fun toAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, ProfileEditDialog.PHOTO_FROM_GALLERY)
    }

    private fun permission() {
        val permissionList = arrayListOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        var size = permissionList.size
        var i = 0
        while (i < size) {
            if (ActivityCompat.checkSelfPermission(
                    TimeMasterApplication.instance.applicationContext,
                    permissionList[i]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.removeAt(i)
                i -= 1
                size -= 1
            }
            i += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions(
            (activity as AppCompatActivity),
            permissionList.toArray(array),
            0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ProfileEditDialog.PHOTO_FROM_GALLERY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val uri = data!!.data
                        saveUri = uri
                        uploadImage()

//                        binding.imageView.setImageURI(uri)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveUri != null) {
            val uriString = saveUri.toString()
            outState.putString("saveUri", uriString)
        }
    }

    private fun uploadImage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        saveUri?.let {
            ref.putFile(it)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
//                        newRecord.recordImage = it.toString()
                        imageUri = it.toString()
                        viewModel.imagePhoto.value = imageUri
                        bindProfileImage(binding.imageView,imageUri)
                    }
                }
        }
    }
}
