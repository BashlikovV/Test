package by.bashlikovvv.homescreen.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.bashlikovvv.core.base.BaseViewModel
import by.bashlikovvv.core.base.SingleLiveEvent
import by.bashlikovvv.core.domain.model.CustomThrowable
import by.bashlikovvv.core.domain.model.Image
import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.model.ParsedException
import by.bashlikovvv.core.domain.usecase.CheckLocationsDataChangedUseCase
import by.bashlikovvv.core.domain.usecase.GetLocationsUseCase
import by.bashlikovvv.core.domain.usecase.GetSectionNameUseCase
import by.bashlikovvv.core.domain.usecase.GetStringUseCase
import by.bashlikovvv.core.domain.usecase.UpdateLocationUseCase
import by.bashlikovvv.core.domain.usecase.UpdateSectionNameUseCase
import by.bashlikovvv.core.domain.usecase.UploadImageUseCase
import by.bashlikovvv.core.ext.toParsedException
import by.bashlikovvv.homescreen.R
import by.bashlikovvv.homescreen.domain.contract.LocalChanges
import by.bashlikovvv.homescreen.domain.model.ImageState
import by.bashlikovvv.homescreen.domain.model.LocationState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject
import javax.inject.Provider

class HomeScreenViewModel(
    private val getStringUseCase: GetStringUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val checkLocationsDataChangedUseCase: CheckLocationsDataChangedUseCase,
    private val getSectionNameUseCase: GetSectionNameUseCase,
    private val updateSectionNameUseCase: UpdateSectionNameUseCase
) : BaseViewModel() {

    val exceptionsHandler = CoroutineExceptionHandler { _, throwable ->
        processThrowable(throwable)
    }

    private val imageProcessingDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val _exceptionsFlow = SingleLiveEvent<ParsedException>()
    val exceptionsFlow: LiveData<ParsedException> = _exceptionsFlow

    private val _locationsFlow = MutableStateFlow<List<LocationState>>(listOf())

    private var _uiState: Flow<List<LocationState>>
    val uiState get() = _uiState

    private var _sectionNameFlow = MutableStateFlow("")
    val sectionNameFlow = _sectionNameFlow.asStateFlow()

    private val random = ThreadLocalRandom.current()
    private val localChanges = LocalChanges()
    private val _localChangesFlow = MutableStateFlow(OnChange(localChanges, random.nextInt()))

    private val currentLocation: Int
        get() = localChanges.currentLocation

    init {
        loadLocations()
        val originChangesFlow = _localChangesFlow.asStateFlow()
        _uiState = combine(
            originChangesFlow,
            _locationsFlow,
            this::merge
        )
    }

    private fun loadLocations() = launchIO(
        safeAction = {
            _locationsFlow.tryEmit(
                getLocationsUseCase.execute().mapToLocationState()
            )
            _sectionNameFlow.tryEmit(getSectionNameUseCase.execute())
        },
        onError = { processThrowable(it) }
    )

    fun updateLocation(location: LocationState) = launchIO(
        safeAction = {
            updateLocationUseCase.execute(location.idx) {
                it.copy(
                    locationName = location.locationName,
                    images = location.images
                        .filter { img -> img.imageUri.isNotEmpty() }
                        .map { img -> Image(img.imageUri) }
                )
            }
        },
        onError = { throwable -> processThrowable(throwable) }
    )

    fun selectImage(location: Int, image: Int) {
        localChanges.selectImage(location, image)
        updateLocalChangesFlow(true)
    }

    fun unselectImage(location: Int, image: Int) {
        localChanges.unselectImage(location, image)
        updateLocalChangesFlow(true)
    }

    fun addImages(images: List<Uri>) = launchCustom(
        safeAction = {
            val size = _locationsFlow.value[currentLocation].images.size
            images.forEachIndexed { index, uri ->
                val imageIds = size + index
                localChanges.setImageProgress(currentLocation, imageIds)
                updateLocalChangesFlow(true)
                val uploadedImageUri = uploadImageUseCase.execute(uri)
                if (uploadedImageUri == null) {
                    processImageNotLoaded()
                    localChanges.removeImageProgress(currentLocation, imageIds)
                    updateLocalChangesFlow(true)
                } else {
                    val image = Image(uploadedImageUri.toString())

                    updateLocationUseCase.execute(currentLocation) { dbLocation ->
                        val newImages = mutableListOf<Image>()
                        newImages.addAll(dbLocation.images)
                        newImages.add(image)

                        dbLocation.copy(images = newImages)
                    }
                    localChanges.removeImageProgress(currentLocation, imageIds)
                    updateLocalChangesFlow(true)
                }
            }
        },
        onError = { processThrowable(it) },
        dispatcher = imageProcessingDispatcher
    )

    fun containsSelected(): Boolean = localChanges.containsSelected()

    fun clearSelected() {
        localChanges.clearSelectedImages()
        updateLocalChangesFlow(true)
    }

    fun removeImages(location: Int) = launchCustom(
        safeAction = {
            val imagesToRemove = localChanges.getImagesToRemove(location).sortedDescending()
            localChanges.clearSelectedImages()
            updateLocalChangesFlow(true)
            imagesToRemove.forEach { image ->
                localChanges.setImageProgress(currentLocation, image)
                updateLocalChangesFlow(true)
                updateLocationUseCase.execute(location) { dbLocation ->
                    val images = dbLocation.images.filterIndexed { index, _ -> index != image }
                    dbLocation.copy(images = images)
                }
                localChanges.removeImageProgress(location, image)
                updateLocalChangesFlow(true)
            }
        },
        onError = { processThrowable(it) },
        dispatcher = imageProcessingDispatcher
    ).invokeOnCompletion { updateLocalChangesFlow(true) }

    fun updateSectionName(newName: String) {
        launchIO(
            safeAction = {
                updateSectionNameUseCase.execute(newName)
            },
            onError = { processThrowable(it) }
        ).invokeOnCompletion {
            _sectionNameFlow.tryEmit(newName)
        }
    }

    private fun processThrowable(throwable: Throwable) = launch(Dispatchers.Main) {
        when (throwable) {
            is CustomThrowable.ImageUploadingThrowable -> {
                _exceptionsFlow.postValue(
                    ParsedException(
                        title = getStringUseCase.execute(R.string.image_uploading_error),
                        message = getStringUseCase.execute(R.string.image_uploading_error_message)
                    )
                )
            }
            else -> {
                _exceptionsFlow.postValue(
                    throwable.toParsedException(::titleBuilder)
                )
            }
        }
    }

    private fun processImageNotLoaded() {
        processThrowable(CustomThrowable.ImageUploadingThrowable)
    }

    private fun titleBuilder(throwable: Throwable): String {
        return throwable.localizedMessage ?: getStringUseCase
            .execute(by.bashlikovvv.core.R.string.smth_went_wrong)
    }

    private suspend fun merge(changes: OnChange<LocalChanges>, list: List<LocationState>): List<LocationState> {
        val localChanges = changes.value
        val localList = if (checkLocationsDataChangedUseCase.execute(list.mapToLocation())) {
            getLocationsUseCase.execute().mapToLocationState(localChanges)
        } else {
            list
        }
        _locationsFlow.tryEmit(localList)
        return localList.mapIndexed { locationIndex, location ->
            val selected = localChanges.containsSelectedImages(locationIndex)
            location.copy(
                idx = locationIndex,
                locationName = location.locationName,
                images = location.images.getImageStatesFromStates(
                    localChanges, selected, locationIndex
                ),
                isInProgress = localChanges.isLocationInProgress(locationIndex),
                isCurrent = locationIndex == localChanges.currentLocation,
                isRemoveButtonVisible = selected
            )
        }
    }

    private fun List<Location>.mapToLocationState(
        localChanges: LocalChanges = _localChangesFlow.value.value
    ): List<LocationState> {
        return this.mapIndexed { locationIndex, location ->
            val locationSelected = localChanges.containsSelectedImages(locationIndex)
            LocationState(
                idx = locationIndex,
                locationName = location.locationName,
                images = location.images.getImageStatesFromImages(
                    localChanges, locationSelected, locationIndex
                ),
                isInProgress = localChanges.isLocationInProgress(locationIndex),
                isCurrent = locationIndex == localChanges.currentLocation,
                isRemoveButtonVisible = locationSelected
            )
        }
    }

    private fun List<ImageState>.getImageStatesFromStates(
        localChanges: LocalChanges,
        selected: Boolean,
        locationIndex: Int
    ): List<ImageState> {
        val resultList = this.mapIndexed { imageIndex, image ->
            image.copy(
                idx = imageIndex,
                imageUri = image.imageUri,
                isInProgress = localChanges.isImageInProgress(locationIndex, imageIndex),
                showSelected = selected,
                isSelected = localChanges.isImageSelected(locationIndex, imageIndex)
            )
        }.toMutableList()
        localChanges.extraProgressImages(resultList.size).forEach { idx ->
            resultList.add(loadingImage(idx))
        }

        return resultList
    }

    private fun List<Image>.getImageStatesFromImages(
        localChanges: LocalChanges,
        selected: Boolean,
        locationIndex: Int
    ): List<ImageState> {
        val resultList = this.mapIndexed { imageIndex, image ->
            ImageState(
                idx = imageIndex,
                imageUri = image.uri,
                isInProgress = localChanges.isImageInProgress(locationIndex, imageIndex),
                showSelected = selected,
                isSelected = localChanges.isImageSelected(locationIndex, imageIndex)
            )
        }.toMutableList()
        localChanges.extraProgressImages(resultList.size).forEach { idx ->
            resultList.add(loadingImage(idx))
        }

        return resultList
    }

    private fun List<LocationState>.mapToLocation(): List<Location> {
        return this.map { state ->
            Location(
                locationName = state.locationName,
                images = state.images.map { imageState ->
                    Image(imageState.imageUri)
                }
            )
        }
    }

    private fun updateLocalChangesFlow(notify: Boolean) {
        _localChangesFlow.update {
            OnChange(localChanges, if (notify) random.nextInt() else it.customizer)
        }
    }

    private fun loadingImage(idx: Int): ImageState = ImageState(
        idx = idx, imageUri = "", isInProgress = true, showSelected = false, isSelected = false
    )

    class Factory @Inject constructor(
        private val getStringUseCase: Provider<GetStringUseCase>,
        private val getLocationsUseCase: Provider<GetLocationsUseCase>,
        private val uploadImageUseCase: Provider<UploadImageUseCase>,
        private val updateLocationUseCase: Provider<UpdateLocationUseCase>,
        private val checkLocationsDataChangedUseCase: Provider<CheckLocationsDataChangedUseCase>,
        private val getSectionNameUseCase: Provider<GetSectionNameUseCase>,
        private val updateSectionNameUseCase: Provider<UpdateSectionNameUseCase>
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == HomeScreenViewModel::class.java)
            return HomeScreenViewModel(
                getStringUseCase = getStringUseCase.get(),
                getLocationsUseCase = getLocationsUseCase.get(),
                uploadImageUseCase = uploadImageUseCase.get(),
                updateLocationUseCase = updateLocationUseCase.get(),
                checkLocationsDataChangedUseCase = checkLocationsDataChangedUseCase.get(),
                getSectionNameUseCase = getSectionNameUseCase.get(),
                updateSectionNameUseCase = updateSectionNameUseCase.get()
            ) as T
        }

    }

    data class OnChange<T>(val value: T, val customizer: Int) {
        override fun toString(): String {
            return "OnChange(customizer=$customizer)"
        }
    }

}