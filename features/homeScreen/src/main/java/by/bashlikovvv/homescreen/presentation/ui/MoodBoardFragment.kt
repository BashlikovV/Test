package by.bashlikovvv.homescreen.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.bashlikovvv.core.base.BaseFragment
import by.bashlikovvv.homescreen.databinding.FragmentMoodboardBinding
import by.bashlikovvv.homescreen.di.HomeScreenComponentProvider
import by.bashlikovvv.homescreen.presentation.viewmodel.HomeScreenViewModel
import dagger.Lazy
import javax.inject.Inject

class MoodBoardFragment : BaseFragment<FragmentMoodboardBinding>() {

    @Inject
    internal lateinit var viewModelFactory: Lazy<HomeScreenViewModel.Factory>

    private val viewModel: HomeScreenViewModel by viewModels({ requireActivity() }) {
        viewModelFactory.get()
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
    ): FragmentMoodboardBinding {
        return FragmentMoodboardBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

}