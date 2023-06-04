package mollah.yamin.androidImageGallery.ui.adapters

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mollah.yamin.androidImageGallery.R
import mollah.yamin.androidImageGallery.databinding.ImageGalleryListItemBinding
import mollah.yamin.androidImageGallery.databinding.ImagePreviewListItemBinding
import mollah.yamin.androidImageGallery.models.MediaStoreImage
import mollah.yamin.androidImageGallery.models.SelectedImage
import mollah.yamin.androidImageGallery.ui.ImageSelectionViewModel
import java.util.Date

class GalleryImageAdapter constructor(
    private val viewModel: ImageSelectionViewModel
): RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    private var dataList: List<MediaStoreImage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ImageGalleryListItemBinding = ImageGalleryListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    inner class ViewHolder (private val binding: ImageGalleryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = dataList[position]

            Glide.with(binding.root.context)
                .load(item.contentUri)
                .sizeMultiplier(0.6f)
                .centerCrop()
                .into(binding.image)

            val drawable = ContextCompat.getDrawable(binding.root.context,
                if (viewModel.selectedImages.containsKey(item.id))
                    R.drawable.round_check_circle_24
                else R.drawable.outline_circle_24
            )

            binding.mark.setImageDrawable(drawable)

            binding.mark.setOnClickListener {
                if (viewModel.selectedImages.containsKey(item.id)) {
                    viewModel.selectedImages.remove(item.id)
                } else {
                    viewModel.selectedImages[item.id] = SelectedImage(item.id, Date(), item)
                }
                viewModel.imageCount.postValue(viewModel.selectedImages.size)
                notifyItemChanged(position)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<MediaStoreImage>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}
