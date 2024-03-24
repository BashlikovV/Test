package by.bashlikovvv.homescreen.presentation.adapter.location

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.homescreen.R
import by.bashlikovvv.homescreen.databinding.LocationsListItemBinding
import by.bashlikovvv.homescreen.domain.model.ImageState
import by.bashlikovvv.homescreen.domain.model.LocationState
import by.bashlikovvv.homescreen.presentation.adapter.image.ImagesListAdapter

class LocationsItemViewHolder(
    private val binding: LocationsListItemBinding,
    private val callbacks: LocationsItemViewHolderCallbacks,
    private val decorationHorizontal: DividerItemDecoration,
    private val decorationVertical: DividerItemDecoration,
    private val decorationDrawable: Drawable?
) : RecyclerView.ViewHolder(binding.root) {

    private var expanded: Boolean = false

    init {
        binding.root.transitionToStart()
    }

    fun bind(
        item: LocationState,
        imagesListAdapter: ImagesListAdapter
    ) {
        bindProgress(item.isInProgress)
        imagesListAdapter.submitList(item.images)
        binding.root.bindRoot()
        bindLocationTextInputEditText(item)
        bindAddLocationImageView(item.idx)
        decorationDrawable?.let {
            decorationHorizontal.setDrawable(it)
            decorationVertical.setDrawable(it)
        }
        binding.imagesRecyclerView.adapter = imagesListAdapter
        bindImagesRecyclerView(item.images)
        showRemoveButton(item)
        binding.locationTextInputEditText.doOnTextChanged { text, _, _, _ ->
            callbacks.onLocationChanged(item.copy(locationName = text.toString()))
        }
    }

    fun bindProgress(isInProgress: Boolean) {
        if (isInProgress) {
            binding.ll.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.ll.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    fun showRemoveButton(
        item: LocationState,
        visibility: Boolean = item.isRemoveButtonVisible
    ) {
        binding.removeButton.setOnClickListener {
            if (item.isRemoveButtonVisible) {
                callbacks.onRemoveButtonClicked(item.idx)
            }
        }
        expanded = if (visibility) {
            binding.root.transitionToEnd()
            true

        } else {
            binding.root.transitionToStart()

            false
        }
    }

    fun bindImagesRecyclerView(
        newImagesList: List<ImageState>
    ) = binding.imagesRecyclerView.apply {
        (adapter as ImagesListAdapter).submitList(newImagesList)
        binding.imagesRecyclerView.removeItemDecoration(decorationHorizontal)
        binding.imagesRecyclerView.removeItemDecoration(decorationVertical)
        binding.imagesRecyclerView.addItemDecoration(decorationHorizontal, 0)
        binding.imagesRecyclerView.addItemDecoration(decorationVertical, 1)
    }

    private fun bindAddLocationImageView(
        location: Int
    ) = binding.addLocationImageView.apply {
        setOnClickListener { callbacks.onAddImageClicked(location) }
    }

    fun bindLocationTextInputEditText(
        item: LocationState
    ) = binding.locationTextInputEditText.apply {
        if (text.toString() != item.locationName) {
            setTextKeepState(item.locationName)
        }
    }

    private fun MotionLayout.bindRoot() {
        elevation = 0f
    }

    companion object {

        fun from(
            parent: ViewGroup,
            callbacks: LocationsItemViewHolderCallbacks
        ): LocationsItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)

            return LocationsItemViewHolder(
                LocationsListItemBinding.inflate(layoutInflater, parent, false),
                callbacks,
                DividerItemDecoration(parent.context, DividerItemDecoration.HORIZONTAL),
                DividerItemDecoration(parent.context, DividerItemDecoration.VERTICAL),
                ContextCompat.getDrawable(parent.context, R.drawable.image_decoration)
            )
        }

    }

    interface LocationsItemViewHolderCallbacks {

        fun onLocationChanged(location: LocationState)

        fun onAddImageClicked(location: Int)

        fun onRemoveButtonClicked(location: Int)

    }

}