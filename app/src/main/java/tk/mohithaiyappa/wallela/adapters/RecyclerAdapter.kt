package tk.mohithaiyappa.wallela.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.mohithaiyappa.wallela.data.UrlDataStorage
import tk.mohithaiyappa.wallela.adapters.RecyclerAdapter.ImageViewHolder
import tk.mohithaiyappa.wallela.databinding.AlbumLayoutForRvBinding
import tk.mohithaiyappa.wallela.navigation.NavigatorImpl
import tk.mohithaiyappa.wallela.ui.WallelaActivity

class RecyclerAdapter(
    var urlArrayList: ArrayList<UrlDataStorage?>?,
    var mContext: Context,
    val navigator: NavigatorImpl
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
            navigator.gotoFullScreenFragment(ds!!)
//            val intent = Intent(mContext, FullscreenActivity::class.java)
//            intent.putExtra("Url", ds?.hiResUrl)
//            intent.putExtra("lowUrl", ds?.lowResUrl)
//            intent.putExtra("midUrl", ds?.midResUrl)
//            mContext.startActivity(intent)
        }
    }
}