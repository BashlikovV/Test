package by.bashlikovvv.test.presentation.flow

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.ui.NavigationUI
import by.bashlikovvv.core.base.BaseFlowFragment
import by.bashlikovvv.test.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFlowFragment : BaseFlowFragment() {

    override fun getLayoutResource(): Int = R.layout.home_flow_fragment

    override fun getContainerId(): Int = R.id.homeFlowFragmentContainer

    override fun init(root: View) {
        val bottomNavBar = root
            .findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavBar, flowNavController)
    }

    override fun graphRes(): Int = R.navigation.home_navigation

    override fun flowArgs(): Bundle = bundleOf()

}