package by.bashlikovvv.test.presentation.flow

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import by.bashlikovvv.core.base.BaseFlowFragment
import by.bashlikovvv.test.R

class ImageFlowFragment : BaseFlowFragment() {

    private val args: ImageFlowFragmentArgs by navArgs()

    override fun graphRes(): Int = R.navigation.image_navigation

    override fun flowArgs(): Bundle = args.toBundle()

}