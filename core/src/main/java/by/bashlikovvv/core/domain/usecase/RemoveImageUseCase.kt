package by.bashlikovvv.core.domain.usecase

import com.google.firebase.storage.FirebaseStorage

class RemoveImageUseCase(private val firebaseStorage: FirebaseStorage) {

    fun execute(uri: String) {
//        val ref = firebaseStorage.reference.child("images/$filename.png")
    }

}