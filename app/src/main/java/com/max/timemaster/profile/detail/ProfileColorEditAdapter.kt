package com.max.timemaster.profile.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.databinding.ItemProfileColorBinding
import com.max.timemaster.profile.edit.ProfileEditViewModel

class ProfileColorEditAdapter(var viewModel: ProfileEditViewModel) :
    ListAdapter<String, ProfileColorEditAdapter.ProfileColorViewHolder>(
        DiffCallback
    ) {

    class ProfileColorViewHolder(private var binding: ItemProfileColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(colorCode: String, viewModel: ProfileEditViewModel) {
            colorCode.let {
                val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
                val colors = intArrayOf(Color.parseColor("#$colorCode"))
                val colorsStateList = ColorStateList(states, colors)
                binding.imageDetailColor.backgroundTintList = colorsStateList

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
                ), parent, false                  //在有inflate 的地方 要注意   parnt, false
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