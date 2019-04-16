package tk.mohithaiyappa.wallela;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {


    private ArrayList<UrlDataStorage> urlArrayList = new ArrayList<UrlDataStorage>();
    private Context mContext;
    private UrlDataStorage urlDataStorage;
    private String TAG = "RecyclerAdapter";


    public RecyclerAdapter(ArrayList<UrlDataStorage> urlArrayList, Context mContext) {
        this.urlArrayList = urlArrayList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_layout_for_rv, viewGroup, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ImageViewHolder imageViewHolder, int i) {
        urlDataStorage = urlArrayList.get(i);
        imageViewHolder.getSimpleDraweeView().setImageURI(urlDataStorage.getMidResUrl());
    }


    @Override
    public int getItemCount() {
        if (urlArrayList == null || urlArrayList.size() == 0) return 0;
        return urlArrayList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        SimpleDraweeView simpleDraweeView;
        View view;


        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            simpleDraweeView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);

        }


        SimpleDraweeView getSimpleDraweeView() {
            return simpleDraweeView;
        }


        @Override
        public void onClick(View v) {
            UrlDataStorage ds = urlArrayList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, FullscreenActivity.class);
            intent.putExtra("Url", ds.getHiResUrl());
            intent.putExtra("lowUrl", ds.getLowResUrl());
            intent.putExtra("midUrl", ds.getMidResUrl());
            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,simpleDraweeView,"trans");
                mContext.startActivity(intent,options.toBundle());
            }else */
            mContext.startActivity(intent);
        }
    }


}
