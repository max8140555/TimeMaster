package com.max.timemaster.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.max.timemaster.profile.item.ProfileItemFragment


class ProfileAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return ProfileItemFragment(
            ProfileTypeFilter.values()[position]
        )
    }

    override fun getCount() = ProfileTypeFilter.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return ProfileTypeFilter.values()[position].value
    }
}
