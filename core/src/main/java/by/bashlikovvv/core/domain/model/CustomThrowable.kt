package by.bashlikovvv.core.domain.model

sealed class CustomThrowable : Throwable() {

    data object ImageUploadingThrowable : CustomThrowable() {
        private fun readResolve(): Any = ImageUploadingThrowable
    }

}