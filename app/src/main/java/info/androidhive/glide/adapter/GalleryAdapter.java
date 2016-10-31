package info.androidhive.glide.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.androidhive.glide.R;
import info.androidhive.glide.app.AppController;
import info.androidhive.glide.helper.ImageDownloadOkHttpTask;

/**
 * Created by Lincoln on 31/03/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<String> images;
    private Context mContext;
    private int mLibraryType;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail   = (SimpleDraweeView) view.findViewById(R.id.thumbnail);
        }
    }


    public GalleryAdapter(Context context, List<String> images, int libraryType) {
        mContext        = context;
        this.images     = images;
        mLibraryType    = libraryType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView   = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String image    = images.get(position);

        if(mLibraryType == 1) {
            Glide.with(mContext).load(image)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.thumbnail);
        } else if(mLibraryType == 2) {
            Picasso picasso = Picasso.with(mContext);
            picasso.setIndicatorsEnabled(true);
            picasso.load(image).into(holder.thumbnail);
        } else if(mLibraryType == 3) {
            Uri uri = Uri.parse(image);
            holder.thumbnail.setImageURI(uri);
        } else if(mLibraryType == 4) {
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(image, ImageLoader.getImageListener(holder.thumbnail,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        } else if(mLibraryType == 5) {
            new ImageDownloadOkHttpTask().execute(image, holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener  = clickListener;
            gestureDetector     = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child  = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child  = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}