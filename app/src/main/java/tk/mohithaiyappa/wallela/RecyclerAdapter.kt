package tk.mohithaiyappa.wallela

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.mohithaiyappa.wallela.RecyclerAdapter.ImageViewHolder
import tk.mohithaiyappa.wallela.databinding.AlbumLayoutForRvBinding

class RecyclerAdapter(
    var urlArrayList: ArrayList<UrlDataStorage?>?,
    var mContext: Context
    ) : RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ImageViewHolder {
        val binding = AlbumLayoutForRvBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(imageViewHolder: ImageViewHolder, i: Int) {
        imageViewHolder.binding.imageView.setImageURI(urlArrayList!![i]?.midResUrl)
    }

    override fun getItemCount(): Int {
        return urlArrayList?.size ?: 0
    }

    inner class ImageViewHolder(val binding: AlbumLayoutForRvBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener(this::onClick)
        }

        private fun onClick(v: View) {
            val ds = urlArrayList!![adapterPosition]
            val intent = Intent(mContext, FullscreenActivity::class.java)
            intent.putExtra("Url", ds?.hiResUrl)
            intent.putExtra("lowUrl", ds?.lowResUrl)
            intent.putExtra("midUrl", ds?.midResUrl)
            mContext.startActivity(intent)
        }
    }
}