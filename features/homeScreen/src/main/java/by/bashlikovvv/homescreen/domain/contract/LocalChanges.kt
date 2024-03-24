package by.bashlikovvv.homescreen.domain.contract

data class LocalChanges(
    private val idsInProgress: MutableSet<Int> = mutableSetOf(),
    private val idsImagesInProgress: MutableList<MutableSet<Int>> = mutableListOf(),
    private var idsImagesIsSelected: MutableList<MutableSet<Int>> = mutableListOf(),
    var currentLocation: Int = 0
) {
    fun selectImage(locationIds: Int, imageIds: Int) {
        for (i in idsImagesIsSelected.size - 1..locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        idsImagesIsSelected.getOrNull(locationIds)?.add(imageIds)
    }

    fun unselectImage(locationIds: Int, imageIds: Int) {
        for (i in idsImagesIsSelected.size - 1..locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        idsImagesIsSelected.getOrNull(locationIds)?.remove(imageIds)
    }

    fun setImageProgress(locationIds: Int, imageIds: Int) {
        for (i in idsImagesInProgress.size - 1..locationIds) {
            idsImagesInProgress.add(mutableSetOf())
        }
        idsImagesInProgress.getOrNull(locationIds)?.add(imageIds)
    }

    fun extraProgressImages(currentSize: Int): List<Int> {
        val imagesSet = idsImagesInProgress.getOrNull(currentLocation) ?: return listOf()
        val diff = (imagesSet.maxByOrNull { it } ?: 1) + 1 - currentSize
        return if (diff > 0) {
            imagesSet.toList().takeLast(diff)
        } else {
            listOf()
        }
    }

    fun removeImageProgress(locationIds: Int, imageIds: Int) {
        for (i in idsImagesInProgress.size - 1..locationIds) {
            idsImagesInProgress.add(mutableSetOf())
        }
        idsImagesInProgress.getOrNull(locationIds)?.remove(imageIds)
    }

    fun isImageInProgress(locationIds: Int, imageIds: Int): Boolean {
        for (i in idsImagesInProgress.size - 1..locationIds) {
            idsImagesInProgress.add(mutableSetOf())
        }
        return idsImagesInProgress.getOrNull(locationIds)?.contains(imageIds) == true
    }

    fun containsSelectedImages(locationIds: Int): Boolean {
        for (i in idsImagesIsSelected.size - 1..locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        return idsImagesIsSelected.getOrNull(locationIds)?.isNotEmpty() == true
    }

    fun isImageSelected(locationIds: Int, imageIds: Int): Boolean {
        for (i in idsImagesIsSelected.size - 1..locationIds) {
            idsImagesIsSelected.add(mutableSetOf())
        }
        return idsImagesIsSelected.getOrNull(locationIds)?.contains(imageIds) == true
    }

    fun isLocationInProgress(locationIds: Int): Boolean {
        return idsInProgress.contains(locationIds)
    }

    fun containsSelected() = idsImagesIsSelected.map { it.isNotEmpty() }.contains(true)

    fun clearSelectedImages() {
        idsImagesIsSelected = mutableListOf()
    }

    fun getImagesToRemove(locationIds: Int): List<Int> =
        idsImagesIsSelected.getOrNull(locationIds)?.toList() ?: listOf()

}