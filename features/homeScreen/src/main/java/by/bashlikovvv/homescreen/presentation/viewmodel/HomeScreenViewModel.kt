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
import by.bashlikovvv.core.domain.usecase.GetLocationsUseCase
import by.bashlikovvv.core.domain.usecase.GetStringUseCase
import by.bashlikovvv.core.domain.usecase.UpdateLocationUseCase
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
    private val updateLocationUseCase: UpdateLocationUseCase
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

    private val random = ThreadLocalRandom.current()
    private val localChanges = LocalChanges()
    private val localChangesFlow = MutableStateFlow(OnChange(localChanges, random.nextInt()))

    private var _removeImageFlow = SingleLiveEvent<Pair<Int, Int?>>()
    val removeImageFlow: LiveData<Pair<Int, Int?>> = _removeImageFlow

    val currentLocation: Int
        get() = localChanges.currentLocation

    init {
        loadLocations()
        val originChangesFlow = localChangesFlow.asStateFlow()
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
        },
        onError = { processThrowable(it) }
    )

    fun updateLocation(location: LocationState) = launchIO(
        safeAction = {
            updateLocationUseCase.execute(location.idx + 1) {
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
        localChangesFlow.update { OnChange(localChanges, random.nextInt()) }
    }

    fun unselectImage(location: Int, image: Int) {
        localChanges.unselectImage(location, image)
        localChangesFlow.update { OnChange(localChanges, random.nextInt()) }
    }

    fun addImage(uri: Uri) = launchCustom(
        safeAction = {
            val task = uploadImageUseCase.execute(uri)
            if (task == null) {
                processImageNotLoaded()
            } else {
                task.addOnSuccessListener {
                    val image = Image(it.toString())

                    addImageRepository(localChanges.currentLocation, image).invokeOnCompletion {
                        loadLocations()
                    }
                }
            }
        },
        onError = { processThrowable(it) },
        dispatcher = imageProcessingDispatcher
    )

    fun containsSelected(): Boolean = localChanges.containsSelected()

    fun clearSelected() {
        localChanges.clearSelectedImages()
        localChangesFlow.update { OnChange(localChanges, random.nextInt()) }
    }

    fun removeImages(location: Int) = launchMain(
        safeAction = {
            val imagesToRemove = localChanges.getImagesToRemove(location)
            localChanges.clearSelectedImages()
            localChangesFlow.update { OnChange(localChanges, random.nextInt()) }
            imagesToRemove.forEach { image ->
                _removeImageFlow.postValue(location to image)
                updateLocationUseCase.execute(location + 1) { dbLocation ->
                    val images = dbLocation.images.filterIndexed { index, value -> index != image }
                    dbLocation.copy(images = images)
                }
                localChanges.removeImageProgress(location, image)
                localChangesFlow.update { OnChange(localChanges, random.nextInt()) }
            }
            loadLocations()
        },
        onError = { processThrowable(it) }
    )

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
        _removeImageFlow.postValue(localChanges.currentLocation to null)
    }

    private fun titleBuilder(throwable: Throwable): String {
        return throwable.localizedMessage ?: getStringUseCase
            .execute(by.bashlikovvv.core.R.string.smth_went_wrong)
    }

    private fun addImageRepository(location: Int, image: Image) = launchIO(
        safeAction = {
            updateLocationUseCase.execute(location + 1) { dbLocation ->
                val newImages = mutableListOf<Image>().apply {
                    addAll(dbLocation.images)
                    add(image)
                }

                dbLocation.copy(images = newImages)
            }
        },
        onError = { throwable -> processThrowable(throwable) }
    )

    fun loadingImage(id: Int): ImageState = ImageState(
        idx = id, imageUri = "", isInProgress = true, showSelected = false, isSelected = false
    )

    private fun merge(changes: OnChange<LocalChanges>, list: List<LocationState>): List<LocationState> {
        val localChanges = changes.value
        return list.mapIndexed { locationIndex, location ->
            location.copy(
                idx = locationIndex,
                locationName = location.locationName,
                images = location.images
                    .mapIndexed { imageIndex, image ->
                        image.copy(
                            idx = imageIndex,
                            imageUri = image.imageUri,
                            isInProgress = localChanges.isImageInProgress(locationIndex, imageIndex),
                            showSelected = localChanges.containsSelectedImages(locationIndex),
                            isSelected = localChanges.isImageSelected(locationIndex, imageIndex)
                        )
                    },
                isInProgress = localChanges.isLocationInProgress(locationIndex),
                isCurrent = locationIndex == localChanges.currentLocation,
                isRemoveButtonVisible = localChanges.containsSelectedImages(locationIndex)
            )
        }
    }

    private fun List<Location>.mapToLocationState(): List<LocationState> {
        return this.mapIndexed { locationIndex, location ->
            LocationState(
                idx = locationIndex,
                locationName = location.locationName,
                images = location.images.mapIndexed { imageIndex, image ->
                    ImageState(
                        idx = imageIndex,
                        imageUri = image.uri,
                        isInProgress = localChanges.isImageInProgress(locationIndex, imageIndex),
                        showSelected = localChanges.containsSelectedImages(locationIndex),
                        isSelected = localChanges.isImageSelected(locationIndex, imageIndex)
                    )
                },
                isInProgress = localChanges.isLocationInProgress(locationIndex),
                isCurrent = locationIndex == localChanges.currentLocation,
                isRemoveButtonVisible = localChanges.containsSelectedImages(locationIndex)
            )
        }
    }

    class Factory @Inject constructor(
        private val getStringUseCase: Provider<GetStringUseCase>,
        private val getLocationsUseCase: Provider<GetLocationsUseCase>,
        private val uploadImageUseCase: Provider<UploadImageUseCase>,
        private val updateLocationUseCase: Provider<UpdateLocationUseCase>
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == HomeScreenViewModel::class.java)
            return HomeScreenViewModel(
                getStringUseCase = getStringUseCase.get(),
                getLocationsUseCase = getLocationsUseCase.get(),
                uploadImageUseCase = uploadImageUseCase.get(),
                updateLocationUseCase = updateLocationUseCase.get()
            ) as T
        }

    }

    data class OnChange<T>(val value: T, val customizer: Int) {
        override fun toString(): String {
            return "OnChange(customizer=$customizer)"
        }
    }

}