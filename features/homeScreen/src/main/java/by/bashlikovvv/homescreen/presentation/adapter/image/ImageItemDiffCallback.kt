package by.bashlikovvv.homescreen.presentation.adapter.image

import androidx.recyclerview.widget.DiffUtil
import by.bashlikovvv.homescreen.domain.model.ImageState

class ImageItemDiffCallback : DiffUtil.ItemCallback<ImageState>() {
    override fun areItemsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem.idx == newItem.idx
    }

    override fun areContentsTheSame(oldItem: ImageState, newItem: ImageState): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ImageState, newItem: ImageState): Any? {
        return when {
            oldItem.imageUri != newItem.imageUri -> {
                ImagePayload.Image(newItem)
            }
            oldItem.isInProgress != newItem.isInProgress -> {
                ImagePayload.Progress(newItem.isInProgress)
            }
            oldItem.showSelected != newItem.showSelected -> {
                ImagePayload.Selection(newItem)
            }
            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}