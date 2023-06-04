package mollah.yamin.androidImageGallery.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import mollah.yamin.androidImageGallery.R
import mollah.yamin.androidImageGallery.databinding.ImageSelectionActivityBinding
import mollah.yamin.androidImageGallery.models.SelectedImage
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ImageSelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetSelectionCount.container)
        bottomSheetBehavior.isDraggable = false

        binding.bottomSheetSelectionCount.btnAdd.setOnClickListener {
            val selectedImages: ArrayList<SelectedImage> = ArrayList()
            for (value in viewModel.selectedImages.values) {
                selectedImages.add(value)
            }
            selectedImages.sortWith { item1, item2 ->
                item2.time.compareTo(item1.time)
            }

            val list: ArrayList<String> = ArrayList()
            for (item in selectedImages) {
                list.add(item.image.contentUri.toString())
            }
            val nIntent = Intent()
            nIntent.putStringArrayListExtra("data", list)
            setResult(Activity.RESULT_OK, nIntent)
            finish()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }

        viewModel.imageCount.observe(this) {
            binding.bottomSheetSelectionCount.label.text = "$it Selected"
            binding.bottomSheetSelectionCount.btnAdd.text = "Add($it)"
            if (it > 0) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        imageAdapter = GalleryImageAdapter(viewModel)
        albumAdapter = GalleryAlbumAdapter {
            showAlbumImages(it)
        }

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

        supportFragmentManager.addOnBackStackChangedListener {
            val directoryOpen = supportFragmentManager.backStackEntryCount > 0
            supportActionBar?.let { actionBar ->
                actionBar.setDisplayHomeAsUpEnabled(directoryOpen)
                actionBar.setDisplayShowHomeEnabled(directoryOpen)
            }
        }

        viewModel.loadImages()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return false
    }

    private fun showAlbumImages(albumName: String) {
        supportFragmentManager.commit {
            val galleryFragment = GalleryFragment.newInstance(albumName)
            replace(R.id.container, galleryFragment, albumName)
            addToBackStack(albumName)
        }
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