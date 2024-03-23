package by.bashlikovvv.core.ext

import android.os.Build
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import by.bashlikovvv.core.domain.model.FlowDestinations

const val REQ_KEY_MAIN_DESTINATION = "mainNavigationResult"

private const val PARAM_DATA = "bundleData"
/**
 * Indicates the desire to open a different destination
 */
fun Fragment.navigateToFlow(flowDestinations : FlowDestinations) {
    requireActivity()
        .supportFragmentManager
        .setFragmentResult(
            REQ_KEY_MAIN_DESTINATION,
            bundleOf(PARAM_DATA to flowDestinations)
        )
}

/**
 * Some sugar to simplify how the fragment listener is set. In this way we use a fixed key for navigation events while the fragment can still use Result API
 * to deliver other results which are not related to navigation
 */
fun FragmentManager.setFragmentNavigationListener(
    lifecycleOwner: LifecycleOwner,
    listener: (flowDestinations : FlowDestinations) -> Any
) {
    setFragmentResultListener(REQ_KEY_MAIN_DESTINATION, lifecycleOwner) { _, bundle ->
        val flowDestinations = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(PARAM_DATA, FlowDestinations::class.java)!!
        } else {
            bundle.getParcelable(PARAM_DATA)!!
        }
        listener.invoke(flowDestinations)
    }
}