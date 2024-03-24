package by.bashlikovvv.homescreen.presentation.adapter.image

import androidx.recyclerview.widget.DiffUtil
import by.bashlikovvv.homescreen.domain.model.ImageState

class ImageItemDiffCallback : DiffUtil.ItemCallback<ImageState>() {
    override fun areItemsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ImageState, newItem: ImageState): Any? {
        when {
            oldItem.isInProgress != newItem.isInProgress -> {
                ImagePayload.Progress(newItem.isInProgress)
            }
            oldItem.showSelected != newItem.showSelected -> {
                ImagePayload.Edition(newItem.showSelected)
            }
            oldItem.isSelected != newItem.isSelected -> {
                ImagePayload.Selection(newItem.isSelected)
            }
        }
        return super.getChangePayload(oldItem, newItem)
    }
}