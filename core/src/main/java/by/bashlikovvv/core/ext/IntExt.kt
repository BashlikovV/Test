package by.bashlikovvv.core.ext

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

fun Int.dp(resources: Resources): Int = TypedValue.applyDimension(
    COMPLEX_UNIT_DIP,
    this + 0.5f,
    resources.displayMetrics
).toInt()

fun Float.dp(resources: Resources): Int = TypedValue.applyDimension(
    COMPLEX_UNIT_DIP,
    this,
    resources.displayMetrics
).toInt()