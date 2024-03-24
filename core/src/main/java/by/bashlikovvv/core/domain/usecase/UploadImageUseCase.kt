package by.bashlikovvv.core.domain.usecase

import android.net.ConnectivityManager
import android.net.Uri
import by.bashlikovvv.core.ext.isConnected
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class UploadImageUseCase(
    private val firebaseStorage: FirebaseStorage,
    private val cm: ConnectivityManager?
) {

    fun execute(imageUri: Uri): Task<Uri>?  {
        return if (cm.isConnected()) {
            val filename = imageUri.lastPathSegment.toString()
            val ref = firebaseStorage.reference.child("images/$filename.png")

            ref.putFile(imageUri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }
        } else {
            null
        }
    }

}