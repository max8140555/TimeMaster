package com.max.timemaster.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.MainViewModel

import com.max.timemaster.R
import com.max.timemaster.bindProfileImage
import com.max.timemaster.databinding.FragmentFavoriteBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.stampToDate
import com.max.timemaster.util.TimeUtil.stampToDateNoYear
import com.max.timemaster.util.UserManager
import kotlinx.android.synthetic.main.item_calendar.view.*
import java.util.*

class FavoriteFragment : Fragment() {


    private val viewModel by viewModels<FavoriteViewModel> {
        getVmFactory()
    }
    lateinit var binding: FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_favoriteDetailDialog)
        }
        val adapter = FavoriteAdapter()
        binding.recyclerFavorite.adapter = adapter

//        adapter.submitList(viewModel.fakerFavorite.value)

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    val selectAttendeeInfo = UserManager.myDate.value?.filter { attendeeInfo ->
                        attendeeInfo.name == it
                    }
                    selectAttendeeInfo?.let { list ->
                        if (list.isNotEmpty()) {
                            binding.textDateName.text = list[0].name
                            binding.textDateBirthday.text =
                                list[0].birthday?.let { birthday ->
                                    stampToDateNoYear(
                                        birthday,
                                        Locale.TAIWAN
                                    )
                                }
                           bindProfileImage(binding.imageProfileAvatar ,list[0].image)
                            //還要加  adapter.submitList
                        }else{
                            //如果選擇ALL的時候
                        }
                }

//                        selectAttendeeInfo?.map {infoName ->
//                        infoName.name
//                    }.toString()

//                    val x = selectAttendeeInfo?.map { infoBirthday ->
//                        infoBirthday.birthday
//                    }
//                    binding.textDateBirthday.text = stampToDateNoYear(x, Locale.TAIWAN)
                }
            }
        })

        return binding.root
    }


}
