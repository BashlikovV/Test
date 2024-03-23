package by.bashlikovvv.homescreen.presentation.adapter.location

import by.bashlikovvv.homescreen.domain.model.ImageState
import by.bashlikovvv.homescreen.domain.model.LocationState

sealed interface LocationPayload {

    data class Progress(val value: Boolean) : LocationPayload

    data class RemoveButton(val value: Boolean) : LocationPayload

    data class Images(val value: List<ImageState>) : LocationPayload

    data class LocationName(val value: LocationState) : LocationPayload

}