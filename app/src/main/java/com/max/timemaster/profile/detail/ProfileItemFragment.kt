package com.max.timemaster.profile.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.max.timemaster.NavigationDirections
import com.max.timemaster.databinding.FragmentProfileItemBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.profile.ProfileTypeFilter
import com.max.timemaster.util.UserManager


class ProfileItemFragment(private val profileType: ProfileTypeFilter) : Fragment() {


    private val viewModel by viewModels<ProfileItemViewModel> { getVmFactory(profileType) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileItemBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ProfileItemAdapter(ProfileItemAdapter.OnClickListener{ myDate->
            let {
                findNavController().navigate(NavigationDirections.navigateToProfileEditDialog(myDate))
            }
        })

        binding.recyclerProfileItem.adapter = adapter
        UserManager.myDate.observe(viewLifecycleOwner, Observer {

            when (profileType) {
                ProfileTypeFilter.CHASE -> {
                    adapter.submitList(it.filter { date ->
                        date.active == true
                    })
                }
                else -> {
                    adapter.submitList(it.filter { date ->
                        date.active == false
                    })
                }
            }
            Log.d("XXXXX", "${UserManager.myDate.value}")
        })

        return binding.root
    }


}