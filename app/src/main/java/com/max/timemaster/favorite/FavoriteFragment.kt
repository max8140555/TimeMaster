package com.max.timemaster.favorite

import android.os.Bundle
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
import com.max.timemaster.R
import com.max.timemaster.bindProfileImage
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.FragmentFavoriteBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.TimeUtil.stampToDateNoYear
import com.max.timemaster.util.UserManager
import java.util.*


private const val NOT_ATTENDEE = 0
private const val SELECT_ATTENDEE = 1
private const val PROMPT_GONE = 2

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

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer { attendee ->
            attendee?.let {

                viewModel.dateFavorite.observe(viewLifecycleOwner, Observer {
                    it?.let { dateFavorite ->

                        UserManager.dateFavorite.value = it

                        val selectDateFavorite = dateFavorite.filter { date ->
                            date.attendeeName == attendee
                        }

                        promptVisibility(selectDateFavorite)

                        if (attendee.isNotEmpty()) {

                            setAttendeeInfo(attendee)
                            adapter.submitList(selectDateFavorite)
                            adapter.notifyDataSetChanged()
                            binding.btnAdd.visibility = VISIBLE
                            binding.layoutFavoriteHeader.visibility = VISIBLE

                        } else {

                            mainViewModel.liveMyDate.observe(viewLifecycleOwner,
                                Observer { listDate ->
                                    if (listDate.isNullOrEmpty()) {
                                        checkHaveDatePrompt(NOT_ATTENDEE)
                                    } else {
                                        if (viewModel.getActiveAttendeeFavorite().isNullOrEmpty()) {
                                            checkHaveDatePrompt(SELECT_ATTENDEE)
                                        } else {
                                            checkHaveDatePrompt(PROMPT_GONE)
                                        }
                                    }
                                })
                            adapter.submitList(viewModel.getActiveAttendeeFavorite())
                            adapter.notifyDataSetChanged()
                            binding.btnAdd.visibility = GONE
                            binding.layoutFavoriteHeader.visibility = GONE

                        }
                    }
                })
            }
        })
        return binding.root
    }

    private fun setAttendeeInfo(attendee: String){
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
    }

    private fun checkHaveDatePrompt(state: Int?) {
        when (state) {
            NOT_ATTENDEE -> {
                binding.prompt.visibility = VISIBLE
                binding.prompt.text = getString(R.string.hint_add_date_text)
            }
            SELECT_ATTENDEE -> {
                binding.prompt.visibility = VISIBLE
                binding.prompt.text = getString(R.string.hint_select_date_text)
                binding.imagePrompt.visibility = GONE
            }
            PROMPT_GONE -> {
                binding.imagePrompt.visibility = GONE
                binding.prompt.visibility = GONE
            }
        }
    }

    private fun promptVisibility(dateCost: List<DateFavorite>?) {
        if (dateCost.isNullOrEmpty()) {

            binding.imagePrompt.setImageResource(R.drawable.icon_add)
            binding.prompt.text = getString(R.string.hint_add_favorite_text)
            binding.imagePrompt.visibility = VISIBLE
            binding.prompt.visibility = VISIBLE

            binding.imagePrompt.setOnClickListener {
                findNavController().navigate(R.id.navigate_to_favoriteDetailDialog)
            }
        } else {
            binding.imagePrompt.visibility = GONE
            binding.prompt.visibility = GONE
        }
    }
}
