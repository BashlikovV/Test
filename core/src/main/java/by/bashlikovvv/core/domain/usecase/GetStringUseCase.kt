package by.bashlikovvv.core.domain.usecase

import android.app.Application
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class GetStringUseCase(private val application: Application) {

    fun execute(@StringRes stringRes: Int): String {
        return ContextCompat.getString(application, stringRes)
    }

}