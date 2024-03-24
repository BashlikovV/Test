package by.bashlikovvv.homescreen.domain.model

data class LocationState(
    val idx: Int,
    val locationName: String,
    var images: List<ImageState>,
    val isInProgress: Boolean,
    val isCurrent: Boolean,
    val isRemoveButtonVisible: Boolean
)