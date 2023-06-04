package mollah.yamin.androidImageGallery.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mollah.yamin.androidImageGallery.databinding.ImageGalleryListItemBinding
import mollah.yamin.androidImageGallery.databinding.ImagePreviewListItemBinding
import mollah.yamin.androidImageGallery.models.MediaStoreImage

class GalleryImageAdapter: RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

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
