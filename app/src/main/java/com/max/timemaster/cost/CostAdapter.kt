package com.max.timemaster.cost

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.DateCost
import com.max.timemaster.databinding.ItemCalendarBinding

class CostAdapter() :
    ListAdapter<DateCost, CostAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */

    //1.ViewHolder 畫布
    class ProductDetailedEvaluationViewHolder(private var binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(dateCost: DateCost) {
            binding.title.text = dateCost.costTitle
            binding.time.text = "07/14"

            binding.content.text = "NT : ${dateCost.costPrice.toString()}"



            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<DateCost>() {
        override fun areItemsTheSame(
            oldItem: DateCost,
            newItem: DateCost
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DateCost,
            newItem: DateCost
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
            ItemCalendarBinding.inflate(
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