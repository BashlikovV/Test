package by.bashlikovvv.homescreen.presentation.adapter.image

import androidx.recyclerview.widget.DiffUtil
import by.bashlikovvv.homescreen.domain.model.ImageState

object ImageItemDiffCallback : DiffUtil.ItemCallback<ImageState>() {
    override fun areItemsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem == newItem
    }
}