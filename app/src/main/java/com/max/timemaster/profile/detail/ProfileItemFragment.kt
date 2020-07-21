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
import com.max.timemaster.network.LoadApiStatus
import com.max.timemaster.profile.ProfileTypeFilter
import com.max.timemaster.util.UserManager

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class ProfileItemFragment(private val profileType: ProfileTypeFilter) : Fragment() {

    /**
     * Lazily initialize our [CatalogItemViewModel].
     */
    private val viewModel by viewModels<ProfileItemViewModel> { getVmFactory(profileType) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileItemBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = ProfileItemAdapter(ProfileItemAdapter.OnClickListener{
            let {
                findNavController().navigate(NavigationDirections.navigateToProfileEditDialog())
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


//        binding.recyclerCatalogItem.adapter = PagingAdapter(PagingAdapter.OnClickListener {
//            viewModel.navigateToDetail(it)
//        })

//        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
//                viewModel.onDetailNavigated()
//            }
//        })

//        viewModel.pagingDataProducts.observe(viewLifecycleOwner, Observer {
//            (binding.recyclerCatalogItem.adapter as PagingAdapter).submitList(it)
//        })

//        binding.layoutSwipeRefreshCatalogItem.setOnRefreshListener {
//            viewModel.refresh()
//        }
//
//        viewModel.status.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if (it != LoadApiStatus.LOADING)
//                    binding.layoutSwipeRefreshCatalogItem.isRefreshing = false
//            }
//        })

        return binding.root
    }
//    class OnClickListener(val clickListener: (product: Product) -> Unit) {
//        fun onClick(product: Product) = clickListener(product)
//    }
}