package com.max.timemaster.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.ItemCalendarBinding
import com.max.timemaster.databinding.ItemFavoriteBinding
import com.max.timemaster.util.TimeUtil.stampToDateTime
import java.util.*

class FavoriteAdapter() :
    ListAdapter<DateFavorite, FavoriteAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dateFavorite: DateFavorite) {
            binding.favoriteTag.text = dateFavorite.favoriteTitle



            binding.executePendingBindings()
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
    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val product =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholderholder.itemView.setOnClickListener {
        holder.bind(product)
    }

}