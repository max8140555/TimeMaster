package com.max.timemaster.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.max.timemaster.MainViewModel
import com.max.timemaster.NavigationDirections

import com.max.timemaster.R
import com.max.timemaster.bindProfileImage
import com.max.timemaster.data.DateFavorite
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
        viewModel.getLiveDateCostResult()



        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer { attendee ->
            attendee?.let {

                viewModel.dateFavorite.observe(viewLifecycleOwner, Observer {
                    it?.let { dateFavorite ->
                        UserManager.dateFavorite.value = viewModel.dateFavorite.value
                        val dateSelect = dateFavorite.filter { date ->
                            date.attendeeName == attendee
                        }

                        if (dateSelect.isNullOrEmpty()){
                            binding.imagePrompt.setImageResource(R.drawable.icon_add)
                            binding.prompt.text = "點擊按鈕紀錄對象喜歡什麼吧！"
                            binding.imagePrompt.visibility = VISIBLE
                            binding.prompt.visibility = VISIBLE
                        }else{
                            binding.imagePrompt.visibility = GONE
                            binding.prompt.visibility = GONE
                        }

                        if (attendee.isNotEmpty()) {

                            adapter.submitList(dateSelect)
                            adapter.notifyDataSetChanged()

                            val selectAttendeeInfo =
                                UserManager.myDate.value?.filter { attendeeInfo ->
                                    attendeeInfo.name == attendee
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

                                    bindProfileImage(binding.imageProfileAvatar, list[0].image)
                                }
                            }

                            binding.btnAdd.visibility = VISIBLE
                            binding.layoutFavoriteHeader.visibility = VISIBLE

                        } else {
                            binding.btnAdd.visibility = GONE
                            binding.layoutFavoriteHeader.visibility = GONE

                            val date = UserManager.myDate.value?.filter { myDate ->
                                myDate.active == true
                            }?.map {
                                it.name
                            }

                            val list = mutableListOf<DateFavorite>()

                            if (date != null) {
                                for (x in date.indices) {
                                    val item =
                                        viewModel.dateFavorite.value?.filter { dateFavorite ->
                                            dateFavorite.attendeeName == date[x]
                                        }
                                    if (!item.isNullOrEmpty()) {
                                        list.addAll(item)
                                    }
                                }
                            }
                            if (list.isNullOrEmpty()){
                                binding.prompt.text = "點左上角按鈕，選擇對象紀錄他喜歡什麼吧！"
                                binding.imagePrompt.setImageResource(R.drawable.toolbar_menu)
                                binding.imagePrompt.visibility = VISIBLE
                                binding.prompt.visibility = VISIBLE
                            }else{
                                binding.imagePrompt.visibility = GONE
                                binding.prompt.visibility = GONE
                            }
                            adapter.submitList(list)
                            adapter.notifyDataSetChanged()

                        }

                    }
                })

            }
        })

        return binding.root
    }


}
