package by.bashlikovvv.homescreen.domain.contract

import android.util.Log

data class LocalChanges(
    private val idsInProgress: MutableSet<Int> = mutableSetOf(),
    private val idsImagesInProgress: MutableList<MutableSet<Int>> = mutableListOf(),
    private var idsImagesIsSelected: MutableList<MutableSet<Int>> = mutableListOf(),
    private var idsImagesToRemove: MutableList<MutableSet<Int>> = mutableListOf(),
    var currentLocation: Int = 0
) {

    fun setLocationProgress(locationIds: Int) {
        if (!idsInProgress.add(locationIds)) {
            idsInProgress.remove(locationIds)
        }
    }

    fun selectImage(locationIds: Int, imageIds: Int) {
        for (i in idsImagesIsSelected.size - 1 .. locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        idsImagesIsSelected[locationIds].add(imageIds)
    }

    fun unselectImage(locationIds: Int, imageIds: Int) {
        for (i in idsImagesIsSelected.size - 1 .. locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        idsImagesIsSelected[locationIds].remove(imageIds)
        Log.i("MYTAG", "ui: ${idsImagesInProgress.joinToString()}")
    }

    fun setImageProgress(locationIds: Int, imageIds: Int) {
        for (i in idsImagesInProgress.size - 1 .. locationIds) {
            idsImagesInProgress.add(mutableSetOf())
        }
        if (!idsImagesInProgress[locationIds].add(imageIds)) {
            idsImagesInProgress[locationIds].remove(imageIds)
        }
    }

    fun isImageInProgress(locationIds: Int, imageIds: Int): Boolean {
        for (i in idsImagesInProgress.size - 1 .. locationIds) {
            idsImagesInProgress.add(mutableSetOf())
        }
        return !idsImagesInProgress[locationIds].contains(imageIds)
    }

    fun containsSelectedImages(locationIds: Int): Boolean {
        for (i in idsImagesIsSelected.size - 1 .. locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        return idsImagesIsSelected[locationIds].isNotEmpty()
    }

    fun isImageSelected(locationIds: Int, imageIds: Int): Boolean {
        for (i in idsImagesIsSelected.size - 1 .. locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        return idsImagesIsSelected[locationIds].contains(imageIds)
    }

    fun isLocationInProgress(locationIds: Int): Boolean {
        return idsInProgress.contains(locationIds)
    }

    fun containsSelected() = idsImagesIsSelected.map { it.isNotEmpty() }.contains(true)

    fun clearSelectedImages() {
        idsImagesIsSelected = mutableListOf()
    }

    fun removeImages(locationIds: Int) {
        idsImagesIsSelected[locationIds].forEach {
            removeImage(locationIds, it)
        }
    }

    private fun removeImage(locationIds: Int, imageIds: Int) {
        for (i in idsImagesToRemove.size - 1 .. locationIds) {
            idsImagesToRemove.add(mutableSetOf())
        }
        idsImagesToRemove[locationIds].add(imageIds)
    }

    fun isImageToRemove(locationIds: Int, imageIds: Int): Boolean {
        for (i in idsImagesToRemove.size - 1 .. locationIds) {
            idsImagesToRemove.add(mutableSetOf())
        }
        return idsImagesToRemove[locationIds].contains(imageIds)
    }

    fun setImageRemoved(locationIds: Int, imageIds: Int) {
        for (i in idsImagesToRemove.size - 1 .. locationIds) {
            idsImagesToRemove.add(mutableSetOf())
        }
        idsImagesToRemove[locationIds].remove(imageIds)
        idsImagesIsSelected[locationIds].remove(imageIds)
    }

}