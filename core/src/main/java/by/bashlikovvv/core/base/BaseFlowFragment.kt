package by.bashlikovvv.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.bashlikovvv.core.R

abstract class BaseFlowFragment : Fragment() {

    private var isGraphAttached: Boolean = false

    private var _navController: NavController? = null

    val flowNavController: NavController
        get() = requireNotNull(_navController)

    @LayoutRes
    open fun getLayoutResource(): Int = R.layout.flow_fragment

    @IdRes
    open fun getContainerId(): Int = R.id.fragmentContainerFlow

    open fun init(root: View) {  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(getLayoutResource(), container, false)
        val host = childFragmentManager.findFragmentById(getContainerId()) as NavHostFragment
        _navController = host.navController

        init(root)

        return root
    }

    @NavigationRes
    abstract fun graphRes(): Int

    abstract fun flowArgs(): Bundle?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isGraphAttached) {
            val args = flowArgs()
            isGraphAttached = if (args == null) {
                flowNavController.setGraph(graphRes())

                true
            } else {
                flowNavController.setGraph(graphRes(), args)

                true
            }
        }
    }

}