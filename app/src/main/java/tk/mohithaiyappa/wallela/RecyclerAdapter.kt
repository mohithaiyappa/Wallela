package tk.mohithaiyappa.wallela

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import tk.mohithaiyappa.wallela.FullscreenActivity
import tk.mohithaiyappa.wallela.RecyclerAdapter.ImageViewHolder

class RecyclerAdapter(urlArrayList: ArrayList<UrlDataStorage?>?, mContext: Context) :
    RecyclerView.Adapter<ImageViewHolder>() {
    private var urlArrayList: ArrayList<UrlDataStorage?>? = ArrayList()
    private val mContext: Context
    private var urlDataStorage: UrlDataStorage? = null
    private val TAG = "RecyclerAdapter"
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ImageViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.album_layout_for_rv, viewGroup, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(imageViewHolder: ImageViewHolder, i: Int) {
        urlDataStorage = urlArrayList!![i]
        imageViewHolder.simpleDraweeView.setImageURI(urlDataStorage?.midResUrl)
    }

    override fun getItemCount(): Int {
        return if (urlArrayList == null || urlArrayList!!.size == 0) 0 else urlArrayList!!.size
    }

    inner class ImageViewHolder internal constructor(var view: View) : RecyclerView.ViewHolder(
        view
    ), View.OnClickListener {
        var simpleDraweeView: SimpleDraweeView
        override fun onClick(v: View) {
            val ds = urlArrayList!![adapterPosition]
            val intent = Intent(mContext, FullscreenActivity::class.java)
            intent.putExtra("Url", ds?.hiResUrl)
            intent.putExtra("lowUrl", ds?.lowResUrl)
            intent.putExtra("midUrl", ds?.midResUrl)
            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,simpleDraweeView,"trans");
                mContext.startActivity(intent,options.toBundle());
            }else */mContext.startActivity(intent)
        }

        init {
            simpleDraweeView = itemView.findViewById(R.id.image_view)
            itemView.setOnClickListener(this)
        }
    }

    init {
        this.urlArrayList = urlArrayList
        this.mContext = mContext
    }
}