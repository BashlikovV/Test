package by.bashlikovvv.homescreen.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.bashlikovvv.core.base.BaseFragment
import by.bashlikovvv.core.domain.model.FlowDestinations
import by.bashlikovvv.core.ext.launchMain
import by.bashlikovvv.core.ext.navigateToFlow
import by.bashlikovvv.homescreen.databinding.FragmentLocationsBinding
import by.bashlikovvv.homescreen.di.HomeScreenComponentProvider
import by.bashlikovvv.homescreen.domain.model.LocationState
import by.bashlikovvv.homescreen.presentation.adapter.location.LocationsListAdapter
import by.bashlikovvv.homescreen.presentation.viewmodel.HomeScreenViewModel
import dagger.Lazy
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class LocationsFragment : BaseFragment<FragmentLocationsBinding>() {

    @Inject internal lateinit var viewModelFactory: Lazy<HomeScreenViewModel.Factory>

    private val viewModel: HomeScreenViewModel by viewModels({ requireActivity() }) {
        viewModelFactory.get()
    }

    private val adapter = LocationsListAdapter(locationListAdapterCallbacks())

    private val launcher = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { list ->
        viewModel.addImages(images = list.filter { it.lastPathSegment.toString().isNotEmpty() })
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as HomeScreenComponentProvider)
            .provideHomeScreenComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLocationsBinding {
        return FragmentLocationsBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        setUpFragment()
        setUpLocationsRecyclerView()
        collectViewModelStates()

        return binding.root
    }

    private fun setUpFragment() {
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            onBackPressedCallback = onBackPressedCallback()
        )
        binding.locationTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSectionName(text.toString())
        }
    }

    private fun onBackPressedCallback() = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.containsSelected()) {
                viewModel.clearSelected()
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun setUpLocationsRecyclerView() {
        binding.locationsRecyclerView.adapter = adapter
    }

    private fun collectViewModelStates() {
        launchMain(
            safeAction = {
                viewModel.uiState
                    .collect { adapter.submitList(it) }
            },
            exceptionHandler = viewModel.exceptionsHandler
        )
        launchMain(
            safeAction = {
                viewModel.sectionNameFlow.collectLatest {
                    if (binding.locationTextInputEditText.text.toString() != it) {
                        binding.locationTextInputEditText.setTextKeepState(it)
                    }
                }
            },
            exceptionHandler = viewModel.exceptionsHandler
        )
        viewModel.exceptionsFlow.observe(viewLifecycleOwner) {
            it
                .getAlertDialog(requireContext())
                .show()
        }
    }

    private fun launchImagesViewer() {
        launcher.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private fun locationListAdapterCallbacks(): LocationsListAdapter.Callbacks =
        object : LocationsListAdapter.Callbacks {
            override fun notifyLocationChanged(location: LocationState) {
                viewModel.updateLocation(location)
            }
            override fun notifyAddImageClicked(location: Int) {
                launchImagesViewer()
            }
            override fun notifyImageSelected(image: Int, location: Int) {
                viewModel.selectImage(location, image)
            }
            override fun notifyImageUnselected(image: Int, location: Int) {
                viewModel.unselectImage(location, image)
            }
            override fun notifyOpenImage(image: String) {
                navigateToFlow(FlowDestinations.ImageScreenFlow(image))
            }
            override fun notifyRemoveClicked(location: Int) {
                viewModel.removeImages(location)
            }
        }

}