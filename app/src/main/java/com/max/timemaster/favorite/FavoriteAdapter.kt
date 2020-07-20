package com.max.timemaster.favorite

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.databinding.ItemFavoriteBinding
import com.max.timemaster.util.TimeUtil.stampToDateTime
import com.max.timemaster.util.UserManager
import java.util.*

class FavoriteAdapter() :
    ListAdapter<DateFavorite, FavoriteAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {


    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(dateFavorite: DateFavorite) {
            binding.title.text = dateFavorite.favoriteTitle
            binding.attendee.text = dateFavorite.attendeeName

            val color = UserManager.myDate.value?.filter {
                it.name == dateFavorite.attendeeName
            }!![0].color
            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))
            val colors = intArrayOf(Color.parseColor("#$color"))
            val colorsStateList = ColorStateList(states, colors)
            binding.view.backgroundTintList = colorsStateList

            val chipGroup = binding.chipGroup
            val tagList = dateFavorite.favoriteContent
//            val listContent = viewModel.edListContent
            for (index in tagList!!.indices) {
                val tagName = tagList[index]
                val chip = Chip(chipGroup.context)
//                val paddingDp = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    10f,
//                    resources.displayMetrics
//                ).toInt()
//                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
                chip.text = tagName
                chip.textSize = 12f
                chip.setTextColor(Color.WHITE)
                chip.chipBackgroundColor = colorsStateList
                chip.closeIconTint = ColorStateList(states, intArrayOf(Color.WHITE))
//                chip.setOnClickListener {
//                    chip.isCloseIconEnabled = !chip.isCloseIconEnabled
//                    //Added click listener on close icon to remove tag from ChipGroup
//                    chip.setOnCloseIconClickListener {
//                        tagList.remove(tagName)
//                        chipGroup.removeView(chip)
//                        Log.e ("Connie", tagList.toString())
//                    }
//                }
                chipGroup.addView(chip)
                binding.executePendingBindings()
            }


        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<DateFavorite>() {
        override fun areItemsTheSame(
            oldItem: DateFavorite,
            newItem: DateFavorite
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DateFavorite,
            newItem: DateFavorite
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
    ): ProductDetailedEvaluationViewHolder {
        return ProductDetailedEvaluationViewHolder(
            ItemFavoriteBinding.inflate(
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
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholderholder.itemView.setOnClickListener {
        holder.bind(product)
    }

}