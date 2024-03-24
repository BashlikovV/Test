package by.bashlikovvv.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Suppress("UNUSED")
abstract class BaseViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    inline fun launchIO(
        crossinline safeAction: suspend () -> Unit,
        crossinline onError: (Throwable) -> Unit,
        errorDispatcher: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            this.launch(errorDispatcher) {
                onError.invoke(throwable)
            }
        }

        return this.launch(exceptionHandler + Dispatchers.IO) {
            safeAction.invoke()
        }
    }

    inline fun launchMain(
        crossinline safeAction: suspend () -> Unit,
        crossinline onError: (Throwable) -> Unit
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            this.launch(Dispatchers.Main) {
                onError.invoke(throwable)
            }
        }

        return this.launch(exceptionHandler + Dispatchers.Main) {
            safeAction.invoke()
        }
    }

    inline fun launchDefault(
        crossinline safeAction: suspend () -> Unit,
        crossinline onError: (Throwable) -> Unit,
        errorDispatcher: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            this.launch(errorDispatcher) {
                onError.invoke(throwable)
            }
        }

        return this.launch(exceptionHandler + Dispatchers.Default) {
            safeAction.invoke()
        }
    }

    inline fun launchUnconfined(
        crossinline safeAction: suspend () -> Unit,
        crossinline onError: (Throwable) -> Unit,
        errorDispatcher: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            this.launch(errorDispatcher) {
                onError.invoke(throwable)
            }
        }

        return this.launch(exceptionHandler + Dispatchers.Unconfined) {
            safeAction.invoke()
        }
    }

    inline fun launchCustom(
        crossinline safeAction: suspend () -> Unit,
        crossinline onError: (Throwable) -> Unit,
        dispatcher: CoroutineDispatcher,
        errorDispatcher: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            this.launch(errorDispatcher) {
                onError.invoke(throwable)
            }
        }

        return this.launch(exceptionHandler + dispatcher) {
            safeAction.invoke()
        }
    }

    override fun onCleared() {
        coroutineContext.job.cancel()
        super.onCleared()
    }

}