package by.bashlikovvv.homescreen.presentation.adapter.location

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.homescreen.R
import by.bashlikovvv.homescreen.databinding.LocationsListItemBinding
import by.bashlikovvv.homescreen.domain.model.LocationState
import by.bashlikovvv.homescreen.presentation.adapter.image.ImagesListAdapter

class LocationsItemViewHolder(
    private val binding: LocationsListItemBinding,
    private val callbacks: LocationsItemViewHolderCallbacks,
    private val decorationHorizontal: DividerItemDecoration,
    private val decorationVertical: DividerItemDecoration,
    private val decorationDrawable: Drawable?
) : RecyclerView.ViewHolder(binding.root) {

    private var animating: Boolean = false

    private var expanded: Boolean = false

    private val expandAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, 1.01f).apply {
        duration = ANIMATION_DURATION
        addUpdateListener {
            val progress = it.animatedValue as Float
            val wrapContentHeight = binding.baseLinearLayout.measureWrapContentHeight()
            binding.baseLinearLayout.updateLayoutParams {
                height = (wrapContentHeight * progress).toInt()
            }
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                animating = true
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                animating = false
            }
        })
    }

    fun bind(
        item: LocationState,
        adapter: ImagesListAdapter
    ) {
        bindProgress(item.isInProgress)
        adapter.submitList(item.images)
        binding.root.bindRoot()
        bindLocationTextInputEditText(item)
        bindAddLocationImageView(item.idx)
        decorationDrawable?.let {
            decorationHorizontal.setDrawable(it)
            decorationVertical.setDrawable(it)
        }
        bindImagesRecyclerView(adapter)
        showRemoveButton(item.isRemoveButtonVisible)
        binding.removeButton.setOnClickListener {
            if (item.isRemoveButtonVisible) {
                callbacks.onRemoveButtonClicked(item.idx)
            }
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

    fun showRemoveButton(visibility: Boolean) {
        expanded = when {
            expanded && !visibility -> {
                binding.root.transitionToStart()
                expandAnimator.reverse()
                false
            }
            !visibility -> {
                binding.root.transitionToStart()
                expandAnimator.reverse()
                false
            }
            else -> {
                binding.root.transitionToEnd()
                expandAnimator.start()
                true
            }
        }
    }

    fun bindImagesRecyclerView(
        customAdapter: ImagesListAdapter
    ) = binding.imagesRecyclerView.apply {
        adapter = customAdapter
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
        setText(item.locationName)
        doOnTextChanged { text, _, _, _ ->
            callbacks.onLocationChanged(item.copy(locationName = text.toString()))
        }
    }

    private fun MotionLayout.bindRoot() {
        elevation = 0f
    }

    private fun ViewGroup.measureWrapContentHeight(): Int {
        this.measure(
            View.MeasureSpec
                .makeMeasureSpec((this.parent as View).measuredWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        return measuredHeight
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
                ContextCompat.getDrawable(parent.context, R.drawable.image_decoration),
            )
        }

        const val ANIMATION_DURATION = 300L

    }

    interface LocationsItemViewHolderCallbacks {

        fun onLocationChanged(location: LocationState)

        fun onAddImageClicked(location: Int)

        fun onRemoveButtonClicked(location: Int)

    }

}