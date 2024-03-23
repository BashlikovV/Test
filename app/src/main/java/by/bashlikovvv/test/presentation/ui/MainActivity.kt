package by.bashlikovvv.test.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.bashlikovvv.core.domain.model.FlowDestinations
import by.bashlikovvv.core.ext.setFragmentNavigationListener
import by.bashlikovvv.test.R
import by.bashlikovvv.test.databinding.ActivityMainBinding
import by.bashlikovvv.test.presentation.TestApplication
import by.bashlikovvv.test.presentation.flow.ImageFlowFragmentArgs

class MainActivity : AppCompatActivity() {

    private val navController: NavController
        get() = findNavController(R.id.mainActivityFragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TestApplication)
            .appComponent
            .inject(this)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityFragmentContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setFragmentNavigationListener()

        
    }

    private fun setFragmentNavigationListener() {
        supportFragmentManager.setFragmentNavigationListener(this) { destination ->
            fragmentNavigationListener(destination)
        }
    }

    private fun fragmentNavigationListener(flowDestination: FlowDestinations) {
        when(flowDestination) {
            is FlowDestinations.HomeScreenFlow -> {
                navController.navigate(R.id.homeFlowFragment)
            }
            is FlowDestinations.ImageScreenFlow -> {
                navController.navigate(
                    R.id.imageFlowFragment,
                    ImageFlowFragmentArgs(flowDestination.imageUrl).toBundle()
                )
            }
        }
    }

}