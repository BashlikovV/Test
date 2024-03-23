package by.bashlikovvv.imagescreen.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import by.bashlikovvv.core.ext.flowFragmentArgs
import by.bashlikovvv.imagescreen.R
import com.bumptech.glide.Glide

class ImageFragment : Fragment() {

    private val imageUri: String? by lazy {
        this@ImageFragment.flowFragmentArgs()?.getString("imageUri")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.fragment_image, container, false)

        Glide.with(layout)
            .asDrawable()
            .load(imageUri)
            .into(layout as ImageView)


        return layout
    }

}