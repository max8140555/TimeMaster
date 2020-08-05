package com.max.timemaster.favorite

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.ItemFavoriteBinding
import com.max.timemaster.util.UserManager

class FavoriteAdapter() :
    ListAdapter<DateFavorite, FavoriteAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

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
            val contentList = dateFavorite.favoriteContent
            chipGroup.removeAllViews()

            if (contentList != null) {
                for (index in contentList.indices) {
                    val content = contentList[index]
                    val chip = Chip(chipGroup.context)
                    chip.text = content
                    chip.textSize = 12f
                    chip.setTextColor(Color.BLACK)
                    chip.chipBackgroundColor = colorsStateList
                    chip.closeIconTint = ColorStateList(states, intArrayOf(Color.WHITE))
                    val paddingDp = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        TimeMasterApplication.instance.resources.displayMetrics
                    ).toInt()

                    chip.setPadding(40, paddingDp, paddingDp, paddingDp)

                    chipGroup.addView(chip)
                    binding.executePendingBindings()

                }
            }


        }
    }


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




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailedEvaluationViewHolder {
        return ProductDetailedEvaluationViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }



    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)
        holder.bind(product)
    }

}