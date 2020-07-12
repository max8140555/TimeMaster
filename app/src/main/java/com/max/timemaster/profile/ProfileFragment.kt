package com.max.timemaster.profile


import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController


import com.max.timemaster.R
import com.max.timemaster.databinding.FragmentProfileBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.UserManager

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                btnAddDate.setOnClickListener {
                    findNavController().navigate(R.id.navigate_to_profileDetailFragment)

                }
//               viewpagerProfileDate.let {
//                    tabsProfileDate.setupWithViewPager(it)
//                    it.adapter = CatalogAdapter(childFragmentManager)
//                    it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsProfileDate))
//                }


                return@onCreateView root
            }

    }


}
