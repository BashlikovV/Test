package by.bashlikovvv.selectableimageview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams

class SelectableImageView : ConstraintLayout {

    var isIimageSelected: Boolean = false

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.selectable_image_view, this)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(context, R.layout.selectable_image_view, this)
        setUpCustomAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.selectable_image_view, this)
        setUpCustomAttrs(attrs)
    }

    val imageView: ImageView
        get() = findViewById(R.id.imageView)

    private val indicatorSelectedImageView: ImageView
        get() = findViewById(R.id.indicatorSelectedImageView)

    private val indicatorUnselectedImageView: ImageView
        get() = findViewById(R.id.indicatorUnselectedImageView)

    private val indicatorLayout: FrameLayout
        get() = findViewById(R.id.indicatorFrameLayout)

    fun showIndicator() {
        indicatorLayout.visibility = VISIBLE
    }

    fun hideIndicator() {
        indicatorLayout.visibility = GONE
    }

    fun selectImage() {
        indicatorSelectedImageView.visibility = VISIBLE
        indicatorUnselectedImageView.visibility = GONE
        isIimageSelected = true
    }

    fun unselectImage() {
        indicatorSelectedImageView.visibility = GONE
        indicatorUnselectedImageView.visibility = VISIBLE
        isIimageSelected = false
    }

    private fun setUpCustomAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectableImageView)
        val imageResource = typedArray.getResourceId(R.styleable.SelectableImageView_siv_src, R.drawable.image)
        val imageSize = typedArray.getDimension(R.styleable.SelectableImageView_siv_image_size, 120f)
        val indicatorResource = typedArray.getResourceId(R.styleable.SelectableImageView_siv_indicator_src, R.drawable.image_unselected)
        val indicatorSelectedResource = typedArray.getResourceId(R.styleable.SelectableImageView_siv_indicator_selected_src, R.drawable.image_selected)

        val indicatorSize = typedArray.getDimension(R.styleable.SelectableImageView_siv_indicator_size, 30f)

        imageView.apply {
            if (imageSize != 120f) {
                updateLayoutParams {
                    height = imageSize.toInt()
                    width = imageSize.toInt()
                }
            }
            setImageResource(imageResource)
        }
        indicatorUnselectedImageView.apply {
            updateLayoutParams {
                height = indicatorSize.toInt()
                width = indicatorSize.toInt()
            }
            setImageResource(indicatorResource)
        }
        indicatorSelectedImageView.apply {
            updateLayoutParams {
                height = indicatorSize.toInt()
                width = indicatorSize.toInt()
            }
            setImageResource(indicatorSelectedResource)
        }
        typedArray.recycle()
    }

}