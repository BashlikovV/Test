package by.bashlikovvv.homescreen.presentation.adapter.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.homescreen.domain.model.ImageState
import by.bashlikovvv.selectableimageview.databinding.SelectableImageViewBinding
import com.bumptech.glide.Glide

class ImageViewHolder(
    private val binding: SelectableImageViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var isImageSelected = false

    fun bind(item: ImageState, callbacks: Callbacks) {
        binding.imageView.setImage(item)
        bindEdition(item.showSelected)
        bindSelection(item.isSelected && item.showSelected)
        bindProgress(item.isInProgress)
        binding.imageView.setOnClickListener {
            if (item.showSelected) {
                onLongClick(callbacks, item)
            } else {
                callbacks.onImageClicked(item)
            }
        }
        binding.imageView.setOnLongClickListener {
            onLongClick(callbacks, item)

            true
        }
    }

    fun bindSelection(value: Boolean) {
        if (value) {
            selectImage()
        } else {
            unselectImage()
        }
    }

    fun bindEdition(value: Boolean) {
        if (value) showIndicator() else hideIndicator()
    }

    fun bindProgress(value: Boolean) {
        if (value) showImageProgress() else hideImageProgress()
    }

    private fun showImageProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideImageProgress() {
        binding.progressBar.visibility = View.GONE
    }

    private fun onLongClick(
        callbacks: Callbacks,
        item: ImageState
    ) {
        if (isImageSelected) {
            callbacks.onImageUnselected(item.idx)
        } else {
            callbacks.onImageSelected(item.idx)
        }
    }

    private fun ImageView.setImage(image: ImageState) {
        Glide.with(this)
            .asDrawable()
            .load(image.imageUri)
            .into(this)
    }

    private fun showIndicator() {
        binding.indicatorFrameLayout.visibility = ConstraintLayout.VISIBLE
    }

    private fun hideIndicator() {
        binding.indicatorFrameLayout.visibility = ConstraintLayout.GONE
    }

    private fun selectImage() {
        binding.indicatorSelectedImageView.visibility = ConstraintLayout.VISIBLE
        binding.indicatorUnselectedImageView.visibility = ConstraintLayout.GONE
        isImageSelected = true
    }

    private fun unselectImage() {
        binding.indicatorSelectedImageView.visibility = ConstraintLayout.GONE
        binding.indicatorUnselectedImageView.visibility = ConstraintLayout.VISIBLE
        isImageSelected = false
    }

    companion object {

        fun from(parent: ViewGroup): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return ImageViewHolder(
                SelectableImageViewBinding.inflate(layoutInflater, parent, false)
            )
        }

    }

    interface Callbacks {

        fun onImageSelected(image: Int)

        fun onImageUnselected(image: Int)

        fun onImageClicked(image: ImageState)

    }

}