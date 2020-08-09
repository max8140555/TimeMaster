package com.max.timemaster.profile.edit

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

class ProfileColorEditAdapter(var viewModel: ProfileEditViewModel) :
    ListAdapter<String, ProfileColorEditAdapter.ProfileColorViewHolder>(
        DiffCallback
    ) {

    class ProfileColorViewHolder(private var binding: ItemProfileColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(colorCode: String, viewModel: ProfileEditViewModel) {
            colorCode.let {

                binding.imageDetailColor.backgroundTintList = SetColorStateList.setColorStateList(colorCode)

                if (adapterPosition == viewModel.selectedPosition.value) {
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
        val colorCode =
            getItem(position)

        holder.itemView.setOnClickListener {
            viewModel.selectedPosition.value = position
            viewModel.edColor.value = colorCode
            notifyDataSetChanged()
        }
        holder.bind(colorCode,viewModel)
    }

}