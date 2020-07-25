package com.max.timemaster.profile.detail

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.databinding.ItemProfileColorBinding
import com.max.timemaster.profile.edit.ProfileEditViewModel

class ProfileColorEditAdapter(var viewModel: ProfileEditViewModel) :
    ListAdapter<String, ProfileColorEditAdapter.ProfileColorViewHolder>(
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
        fun bind(colorCode: String, selectedPosition: Int,viewModel: ProfileEditViewModel) {
            colorCode.let {
                binding.imageDetailColor.setBackgroundColor(Color.parseColor("#$colorCode"))

                if (adapterPosition == selectedPosition) {
                    binding.root.foreground = ShapeDrawable(object : Shape() {
                        override fun draw(canvas: Canvas, paint: Paint) {
                            paint.color = Color.WHITE
                            paint.style = Paint.Style.STROKE
                            paint.strokeWidth = TimeMasterApplication.instance.resources
                                .getDimensionPixelSize(R.dimen.edge_add2cart_select_inside)
                                .toFloat()
                            canvas.drawRect(0f, 0f, this.width, this.height, paint)
                            paint.color = Color.BLACK
                            paint.style = Paint.Style.STROKE
                            paint.strokeWidth = TimeMasterApplication.instance.resources
                                .getDimensionPixelSize(R.dimen.edge_add2cart_select).toFloat()
                            canvas.drawRect(0f, 0f, this.width, this.height, paint)
                        }
                    })
                } else {
                    binding.root.foreground = null
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
        val colorCode =
            getItem(position)  //告訴onCreateViewHolder 要生成幾個viewholder
        holder.itemView.setOnClickListener {
            selectedPosition = position
            viewModel.edColor.value = colorCode
            notifyDataSetChanged()
        }
        holder.bind(colorCode,selectedPosition,viewModel)
    }

}