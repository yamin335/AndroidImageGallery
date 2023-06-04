package mollah.yamin.androidImageGallery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commitNow
import mollah.yamin.androidImageGallery.R
import mollah.yamin.androidImageGallery.databinding.ImageSelectionActivityBinding
import mollah.yamin.androidImageGallery.ui.adapters.GalleryAlbumAdapter
import mollah.yamin.androidImageGallery.ui.adapters.GalleryImageAdapter

const val IMAGE_PREVIEW = "image"
const val ALL_IMAGE_PREVIEW = "all_image_preview"
const val ALBUM_PREVIEW = "album"

class ImageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ImageSelectionActivityBinding
    private val viewModel: ImageSelectionViewModel by viewModels()
    private var previewType: String = ALL_IMAGE_PREVIEW
    private lateinit var imageAdapter: GalleryImageAdapter
    private lateinit var albumAdapter: GalleryAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ImageSelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageAdapter = GalleryImageAdapter()
        albumAdapter = GalleryAlbumAdapter()

        loadImageGallery()

        viewModel.images.observe(this) {
            loadImageGallery()
        }

        viewModel.albumImages.observe(this) {
            loadImageGallery()
        }

        binding.photosTabGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            previewType = if (checkedIds.isNotEmpty() && checkedIds.first() == R.id.photos) {
                ALL_IMAGE_PREVIEW
            } else {
                ALBUM_PREVIEW
            }

            loadImageGallery()
        }

        viewModel.loadImages()
    }

    private fun loadImageGallery() {
        when(previewType) {
            ALL_IMAGE_PREVIEW -> {
                showAllImages()
            }
            ALBUM_PREVIEW -> {
                showAllImageAlbums()
            }
        }
    }

    private fun showAllImages() {
        binding.galleryRecycler.apply {
            adapter = imageAdapter
        }
        viewModel.images.value?.let {
            imageAdapter.setData(it)
        }
    }

    private fun showAllImageAlbums() {
        binding.galleryRecycler.apply {
            adapter = albumAdapter
        }
        viewModel.albumImages.value?.let {
            albumAdapter.setData(it)
        }
    }
}