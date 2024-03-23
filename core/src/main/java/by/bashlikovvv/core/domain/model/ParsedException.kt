package by.bashlikovvv.core.domain.model

import android.content.Context
import androidx.appcompat.app.AlertDialog
import by.bashlikovvv.core.R

class ParsedException(
    val title: String,
    val message: String? = null
) {

    fun getAlertDialog(
        context: Context,
        notifyOkClicked: (() -> Unit)? = null,
        notifyCancelClicked: (() -> Unit)? = null
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)

        with(builder) {
            setTitle(title)
            setMessage(message)
            if (notifyOkClicked != null) {
                setPositiveButton(R.string.ok) { dialogInterface, _ ->
                    notifyOkClicked.invoke()
                    dialogInterface.dismiss()
                }
            }
            if (notifyCancelClicked != null) {
                setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                    notifyCancelClicked.invoke()
                    dialogInterface.dismiss()
                }
            }
        }

        return builder.create()
    }

}
