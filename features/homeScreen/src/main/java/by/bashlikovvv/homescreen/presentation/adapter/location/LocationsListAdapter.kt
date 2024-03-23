package by.bashlikovvv.homescreen.presentation.adapter.location

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import by.bashlikovvv.homescreen.domain.model.ImageState
import by.bashlikovvv.homescreen.domain.model.LocationState
import by.bashlikovvv.homescreen.presentation.adapter.image.ImagesListAdapter

class LocationsListAdapter(
    private val callbacks: Callbacks
) : ListAdapter<LocationState, LocationsItemViewHolder>(LocationItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsItemViewHolder {
        return LocationsItemViewHolder(parent, locationsItemViewHolderCallbacks())
    }

    override fun onBindViewHolder(holder: LocationsItemViewHolder, position: Int) {
        holder.bind(
            item = getItem(position),
            adapter = getImagesListAdapter(callbacks, holder.adapterPosition)
        )
    }

    override fun onBindViewHolder(
        holder: LocationsItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (val payload = payloads.lastOrNull()) {
            is LocationPayload.Progress -> holder.bindProgress(payload.value)
            is LocationPayload.RemoveButton -> holder.showRemoveButton(payload.value)
            is LocationPayload.Images -> {
                if (payload.value.map { it.isSelected }.contains(true)) {
                    holder.showRemoveButton(true)
                } else {
                    holder.showRemoveButton(false)
                }
                holder.bindImagesRecyclerView(
                    getImagesListAdapter(callbacks, holder.adapterPosition).apply {
                        submitList(payload.value)
                    }
                )
            }
            is LocationPayload.LocationName -> holder.bindLocationTextInputEditText(payload.value)
            else -> onBindViewHolder(holder, position)
        }
    }

    private fun locationsItemViewHolderCallbacks() =
        object : LocationsItemViewHolder.LocationsItemViewHolderCallbacks {
            override fun onLocationChanged(location: LocationState) {
                callbacks.notifyLocationChanged(location)
            }
            override fun onAddImageClicked(location: Int) {
                callbacks.notifyAddImageClicked(location)
            }
            override fun onRemoveButtonClicked(location: Int) {
                callbacks.notifyRemoveClicked(location)
            }
        }

    private fun getImagesListAdapter(callbacks: Callbacks, position: Int) = ImagesListAdapter(
        callbacks = object : ImagesListAdapter.Callbacks {
            override fun notifyImageSelected(image: Int) {
                callbacks.notifyImageSelected(image, position)
            }

            override fun notifyImageUnselected(image: Int) {
                callbacks.notifyImageUnselected(image, position)
            }
            override fun notifyImageClicked(image: ImageState) {
                callbacks.notifyOpenImage(image)
            }
        }
    )

    interface Callbacks {

        fun notifyLocationChanged(location: LocationState)

        fun notifyAddImageClicked(location: Int)

        fun notifyImageSelected(image: Int, location: Int)

        fun notifyImageUnselected(image: Int, location: Int)

        fun notifyOpenImage(image: ImageState)

        fun notifyRemoveClicked(location: Int)

    }

}