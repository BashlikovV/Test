package by.bashlikovvv.core.domain.usecase

import android.net.ConnectivityManager
import android.net.Uri
import by.bashlikovvv.core.ext.isConnected
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UploadImageUseCase(
    private val firebaseStorage: FirebaseStorage,
    private val cm: ConnectivityManager?
) {

    suspend fun execute(imageUri: Uri): Uri?  {
        return if (cm.isConnected()) {
            val filename = imageUri.lastPathSegment.toString()
            val ref = firebaseStorage.reference.child("images/$filename.png")

            ref.putFile(imageUri)
                .await()
                .storage
                .downloadUrl
                .await()
        } else {
            null
        }
    }

}