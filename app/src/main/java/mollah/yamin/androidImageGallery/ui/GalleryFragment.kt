package mollah.yamin.androidImageGallery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import mollah.yamin.androidImageGallery.R
import mollah.yamin.androidImageGallery.databinding.GalleryFragmentBinding
import mollah.yamin.androidImageGallery.ui.adapters.GalleryAlbumAdapter
import mollah.yamin.androidImageGallery.ui.adapters.GalleryImageAdapter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ALBUM_NAME = "album_name"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class GalleryFragment : Fragment() {

    private var albumName: String? = null
    private lateinit var binding: GalleryFragmentBinding

    // Activity scoped ViewModel
    private val viewModel: ImageSelectionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            albumName = it.getString(ALBUM_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param previewType Parameter 1.
         * @param albumName Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */

        @JvmStatic
        fun newInstance(albumName: String?) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ALBUM_NAME, albumName)
                }
            }
    }
}