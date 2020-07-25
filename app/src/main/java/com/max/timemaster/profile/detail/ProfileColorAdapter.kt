package com.max.timemaster.profile.detail

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.databinding.ItemProfileColorBinding

class ProfileColorAdapter(var viewModel: ProfileDetailViewModel) :
    ListAdapter<String, ProfileColorAdapter.ProfileColorViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    var selectedPosition = -1

    //1.ViewHolder 畫布
    class ProfileColorViewHolder(private var binding: ItemProfileColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(colorCode: String, selectedPosition: Int) {
            colorCode.let {
                val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
                val colors = intArrayOf(Color.parseColor("#$colorCode"))
                val colorsStateList = ColorStateList(states, colors)
                binding.imageDetailColor.backgroundTintList = colorsStateList

                if (adapterPosition == selectedPosition) {
                    binding.imageCorrect.visibility = View.VISIBLE
                } else {
                    binding.imageCorrect.visibility = View.GONE
                }

                binding.executePendingBindings()
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
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

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    //執行畫布

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

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    //2.綁定ViewHolder 畫布
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProfileColorViewHolder, position: Int) {
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder
        holder.itemView.setOnClickListener {
            viewModel.selectedPosition.value = position
            viewModel.edColor.value = product


            notifyDataSetChanged()
        }
        viewModel.selectedPosition.value?.let { holder.bind(product, it) }
    }

}