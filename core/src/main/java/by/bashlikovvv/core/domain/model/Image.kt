package by.bashlikovvv.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val uri: String
) : Parcelable
