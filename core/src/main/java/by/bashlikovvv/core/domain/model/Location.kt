package by.bashlikovvv.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: Int,
    val locationName: String,
    val images: List<Image>
) : Parcelable