package by.bashlikovvv.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class FlowDestinations : Parcelable {

    @Parcelize
    data object HomeScreenFlow : FlowDestinations()

    @Parcelize
    data class ImageScreenFlow(val imageUrl: String) : FlowDestinations()

}