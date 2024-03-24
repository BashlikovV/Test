package by.bashlikovvv.homescreen.presentation.adapter.image

import by.bashlikovvv.homescreen.domain.model.ImageState

sealed class ImagePayload {

    data class Progress(val value: Boolean) : ImagePayload()

    data class Selection(val value: ImageState) : ImagePayload()

    data class Image(val value: ImageState) : ImagePayload()

}