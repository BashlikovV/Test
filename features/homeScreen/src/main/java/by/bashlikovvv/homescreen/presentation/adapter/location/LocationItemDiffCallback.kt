package by.bashlikovvv.homescreen.presentation.adapter.location

import androidx.recyclerview.widget.DiffUtil
import by.bashlikovvv.homescreen.domain.model.LocationState

class LocationItemDiffCallback : DiffUtil.ItemCallback<LocationState>() {
    override fun areItemsTheSame(oldItem: LocationState, newItem: LocationState): Boolean {
        return oldItem.idx == newItem.idx
    }

    override fun areContentsTheSame(oldItem: LocationState, newItem: LocationState): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: LocationState, newItem: LocationState): Any? {
        return when {
            oldItem.isInProgress != newItem.isInProgress -> {
                LocationPayload.Progress(newItem.isInProgress)
            }
            oldItem.images != newItem.images -> {
                if (oldItem.images.size != newItem.images.size
                    && newItem.images.size % 3 == 1
                    || oldItem.images.size % 3 == 0) {
                    super.getChangePayload(oldItem, newItem)
                } else {
                    LocationPayload.Images(newItem.images)
                }
            }
            oldItem.isRemoveButtonVisible != newItem.isRemoveButtonVisible -> {
                LocationPayload.RemoveButton(newItem.isRemoveButtonVisible)
            }
            oldItem.locationName != newItem.locationName -> {
                LocationPayload.LocationName(newItem)
            }
            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}