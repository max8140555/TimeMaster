package com.max.timemaster.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.max.timemaster.R
import com.max.timemaster.databinding.FragmentProfileBinding
import com.max.timemaster.ext.getVmFactory

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        FragmentProfileBinding.inflate(inflater, container, false)
            .apply {

                lifecycleOwner = viewLifecycleOwner
                viewpagerProfileDate.let {
                    tabsProfileDate.setupWithViewPager(it)
                    it.adapter = ProfileAdapter(
                        childFragmentManager
                    )
                    it.addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(tabsProfileDate))
                }

                btnAddDate.setOnClickListener {
                    findNavController().navigate(R.id.navigate_to_profileDetailFragment)
                }

                return@onCreateView root
            }
    }

}
