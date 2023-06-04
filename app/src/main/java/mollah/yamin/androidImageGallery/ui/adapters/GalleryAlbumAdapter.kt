package mollah.yamin.androidImageGallery.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mollah.yamin.androidImageGallery.databinding.AlbumGalleryListItemBinding
import mollah.yamin.androidImageGallery.databinding.ImagePreviewListItemBinding
import mollah.yamin.androidImageGallery.models.ImageAlbum

class GalleryAlbumAdapter: RecyclerView.Adapter<GalleryAlbumAdapter.ViewHolder>() {

    private var dataList: List<ImageAlbum> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AlbumGalleryListItemBinding = AlbumGalleryListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    inner class ViewHolder (private val binding: AlbumGalleryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = dataList[position]

            Glide.with(binding.root.context)
                .load(item.images[0].contentUri)
                .sizeMultiplier(0.6f)
                .centerCrop()
                .into(binding.image)

            binding.albumName.text = item.albumName
            binding.totalImages.text = item.images.size.toString()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<ImageAlbum>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}
