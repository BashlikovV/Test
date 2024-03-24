package by.bashlikovvv.core.ext

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

fun ViewGroup.measureWrapContentHeight(): Int {
    this.measure(
        View.MeasureSpec
            .makeMeasureSpec((this.parent as View).measuredWidth, View.MeasureSpec.EXACTLY),
        View.MeasureSpec
            .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    return measuredHeight
}

fun ViewGroup.wrapContentHeight(): Int {
    val wrapContentHeight = measureWrapContentHeight()
    updateLayoutParams {
        height = wrapContentHeight
    }

    return wrapContentHeight
}