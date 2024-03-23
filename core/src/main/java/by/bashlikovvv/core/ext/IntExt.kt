package by.bashlikovvv.core.ext

import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Int
    get() = kotlin.math.ceil(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toDouble()
    ).toInt()