package by.bashlikovvv.homescreen.presentation.adapter.location

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import by.bashlikovvv.homescreen.domain.model.LocationState
import by.bashlikovvv.homescreen.presentation.adapter.image.ImagesListAdapter

class LocationsListAdapter(
    private val callbacks: Callbacks
) : ListAdapter<LocationState, LocationsItemViewHolder>(LocationItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsItemViewHolder {
        return LocationsItemViewHolder.from(
            parent = parent,
            callbacks = locationsItemViewHolderCallbacks()
        )
    }

    override fun onBindViewHolder(holder: LocationsItemViewHolder, position: Int) {
        holder.bind(
            item = getItem(position),
            imagesListAdapter = ImagesListAdapter(imagesLIstAdapterCallbacks(position))
        )
    }

    override fun onBindViewHolder(
        holder: LocationsItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (val payload = payloads.lastOrNull()) {
            is LocationPayload.Progress -> holder.bindProgress(payload.value)
            is LocationPayload.RemoveButton -> holder.showRemoveButton(getItem(position))
            is LocationPayload.Images -> {
                holder.showRemoveButton(getItem(position))
                holder.bindImagesRecyclerView(payload.value)
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
            override fun onImageSelected(position: Int, image: Int) {
                callbacks.notifyImageSelected(image, position)
            }
            override fun onImageUnselected(position: Int, image: Int) {
                callbacks.notifyImageUnselected(image, position)
            }
            override fun onImageClicked(position: Int, image: String) {
                callbacks.notifyOpenImage(image)
            }
        }

    private fun imagesLIstAdapterCallbacks(location: Int) = object : ImagesListAdapter.Callbacks {
        override fun notifyImageSelected(image: Int) {
            callbacks.notifyImageSelected(image, location)
        }
        override fun notifyImageUnselected(image: Int) {
            callbacks.notifyImageUnselected(image, location)
        }
        override fun notifyImageClicked(image: String) {
            callbacks.notifyRemoveClicked(location)
        }
    }

    interface Callbacks {

        fun notifyLocationChanged(location: LocationState)

        fun notifyAddImageClicked(location: Int)

        fun notifyImageSelected(image: Int, location: Int)

        fun notifyImageUnselected(image: Int, location: Int)

        fun notifyOpenImage(image: String)

        fun notifyRemoveClicked(location: Int)

    }

}