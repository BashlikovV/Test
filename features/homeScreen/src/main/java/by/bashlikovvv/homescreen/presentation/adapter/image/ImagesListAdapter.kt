package by.bashlikovvv.homescreen.presentation.adapter.image

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import by.bashlikovvv.homescreen.domain.model.ImageState

class ImagesListAdapter(
    private val callbacks: Callbacks
) : ListAdapter<ImageState, ImageViewHolder>(ImageItemDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(
            item = getItem(position),
            callbacks = object : ImageViewHolder.Callbacks {
                override fun onImageSelected(image: Int) {
                    callbacks.notifyImageSelected(image)
                }
                override fun onImageUnselected(image: Int) {
                    callbacks.notifyImageUnselected(image)
                }
                override fun onImageClicked(image: ImageState) {
                    callbacks.notifyImageClicked(image)
                }
            }
        )
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (val payload = payloads.lastOrNull()) {
            is ImagePayload.Selection -> { holder.bindSelection(payload.value) }
            is ImagePayload.Edition -> { holder.bindEdition(payload.value) }
            is ImagePayload.Progress -> { holder.bindProgress(payload.value) }
            else -> onBindViewHolder(holder, position)
        }
    }

    interface Callbacks {
        
        fun notifyImageSelected(image: Int)

        fun notifyImageUnselected(image: Int)

        fun notifyImageClicked(image: ImageState)
        
    }

}