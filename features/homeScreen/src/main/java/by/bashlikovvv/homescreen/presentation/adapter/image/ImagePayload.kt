package by.bashlikovvv.homescreen.presentation.adapter.image

sealed class ImagePayload {

    data class Progress(val value: Boolean) : ImagePayload()

    data class Selection(val value: Boolean) : ImagePayload()

    data class Edition(val value: Boolean) : ImagePayload()

}