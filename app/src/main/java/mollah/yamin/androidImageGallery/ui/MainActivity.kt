package mollah.yamin.androidImageGallery.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import mollah.yamin.androidImageGallery.R
import mollah.yamin.androidImageGallery.databinding.MainActivityBinding
import mollah.yamin.androidImageGallery.ui.adapters.ImagePreviewAdapter
import mollah.yamin.androidImageGallery.utils.PermissionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var picFromGalleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var permissionUtils: PermissionUtils

    private lateinit var imagePreviewAdapter: ImagePreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePreviewAdapter = ImagePreviewAdapter()
        binding.imageRecycler.adapter = imagePreviewAdapter

        permissionUtils = PermissionUtils(activity = this, isFragmentContext = false)

        picFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val photoUriList = result?.data?.getStringArrayListExtra("data")
            photoUriList?.let {
                try {
                    if (it.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                        binding.imageRecycler.visibility = View.GONE
                    } else {
                        binding.emptyView.visibility = View.GONE
                        binding.imageRecycler.visibility = View.VISIBLE
                        imagePreviewAdapter.setData(it)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.btnAddImage.setOnClickListener {
            permissionUtils.requestMultiplePermissions(
                permissions = PermissionUtils.onlyGalleryPermission,
                rationaleMsg = getString(R.string.allow_permissions_to_continue),
                object : PermissionUtils.PermissionRequestResultCallback {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionGranted() {
                        selectImageFromGallery()
                    }
                }
            )
        }
    }

    private fun selectImageFromGallery() {
        val galleryIntent = Intent(
            this,
            ImageSelectionActivity::class.java
        )
        picFromGalleryLauncher.launch(galleryIntent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}