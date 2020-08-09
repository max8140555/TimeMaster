package com.max.timemaster.profile.item

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.data.MyDate
import com.max.timemaster.databinding.ItemProfileGridBinding

class ProfileItemAdapter(val onClickListener: OnClickListener) :
    ListAdapter<MyDate, ProfileItemAdapter.ProductDetailedEvaluationViewHolder>(
        DiffCallback
    ) {

    class ProductDetailedEvaluationViewHolder(private var binding: ItemProfileGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myDate: MyDate) {
            binding.textProfileName.text = myDate.name
            binding.imageString = myDate.image
            binding.date = myDate
            binding.imageProfileMain.background.setTint(
                Color.parseColor("#${myDate.color}")
            )
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<MyDate>() {
        override fun areItemsTheSame(
            oldItem: MyDate,
            newItem: MyDate
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MyDate,
            newItem: MyDate
        ): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductDetailedEvaluationViewHolder {
        return ProductDetailedEvaluationViewHolder(
            ItemProfileGridBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ProductDetailedEvaluationViewHolder, position: Int) {
        val myDate =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder
        holder.itemView.setOnClickListener {
            onClickListener.onClick(myDate)
        }
        holder.bind(myDate)
    }

    class OnClickListener(val clickListener: (myDate: MyDate) -> Unit) {
        fun onClick(myDate: MyDate) = clickListener(myDate)
    }
}