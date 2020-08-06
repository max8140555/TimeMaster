package com.max.timemaster.profile.detail


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.databinding.ItemProfileColorBinding
import com.max.timemaster.util.SetColorStateList


class ProfileColorAdapter(var viewModel: ProfileDetailViewModel) :
    ListAdapter<String, ProfileColorAdapter.ProfileColorViewHolder>(
        DiffCallback
    ) {



    class ProfileColorViewHolder(private var binding: ItemProfileColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(colorCode: String, selectedPosition: Int) {
            colorCode.let {
                binding.imageDetailColor.backgroundTintList = SetColorStateList.setColorStateList(colorCode)

                if (adapterPosition == selectedPosition) {
                    binding.imageCorrect.visibility = View.VISIBLE
                } else {
                    binding.imageCorrect.visibility = View.GONE
                }

                binding.executePendingBindings()
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileColorViewHolder {
        return ProfileColorViewHolder(
            ItemProfileColorBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false                  
            )
        )
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProfileColorViewHolder, position: Int) {
        val product =
            getItem(position)  
        holder.itemView.setOnClickListener {
            viewModel.selectedPosition.value = position
            viewModel.edColor.value = product


            notifyDataSetChanged()
        }
        viewModel.selectedPosition.value?.let { holder.bind(product, it) }
    }

}