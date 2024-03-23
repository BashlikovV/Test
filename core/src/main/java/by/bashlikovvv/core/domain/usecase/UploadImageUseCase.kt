package by.bashlikovvv.core.domain.usecase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class UploadImageUseCase(private val firebaseStorage: FirebaseStorage) {

    fun execute(imageUri: Uri): Task<Uri>  {
        val filename = imageUri.lastPathSegment.toString()
        val ref = firebaseStorage.reference.child("images/$filename.png")

        return ref.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }
    }

}