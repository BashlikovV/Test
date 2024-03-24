package by.bashlikovvv.homescreen.domain.model

data class ImageState(
    val idx: Int,
    val imageUri: String,
    var isInProgress: Boolean,
    val showSelected: Boolean,
    val isSelected: Boolean,
)