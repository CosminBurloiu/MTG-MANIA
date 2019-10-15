package com.example.android.mtg_mania.videos;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mtg_mania.MTGMApplicationContext;
import com.example.android.mtg_mania.R;

import java.util.ArrayList;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {

    private ArrayList<VideoItem> items;

    public VideoItemAdapter(ArrayList<VideoItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemAdapter.ViewHolder holder, int position) {

        final VideoItem videoItem = items.get(position);
        final VideoItemAdapter.ViewHolder marcel = holder;
        holder.title.setText(videoItem.getTitle());
        holder.playtime.setText(videoItem.getPlaytime());


        new AsyncTask<String, String, String>() {
            Bitmap bitmapVideoThumbnail;

            @Override
            protected String doInBackground(String... strings) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(MTGMApplicationContext.getAppContext(),videoItem.getUri());

                try {
                    //Your method call here
                    bitmapVideoThumbnail = retriever.getFrameAtTime(200000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String id) {
                super.onPostExecute(id);
                if (bitmapVideoThumbnail != null) {
                    //Load your bitmap here
                    marcel.thumbnail.setImageBitmap(bitmapVideoThumbnail);
                }
            }
        }.execute();


        holder.deleteButton.setTag(videoItem.getUri());
    }

    public void setItems(ArrayList<VideoItem> receivedItems) {
        items = receivedItems;
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView playtime;
        public final ImageView thumbnail;
        public final ImageButton deleteButton;


        public ViewHolder(View view) {
            super(view);
            this.view = view;

            title = view.findViewById(R.id.title);
            playtime = view.findViewById(R.id.play_time);
            thumbnail = view.findViewById(R.id.thumbnail);

            deleteButton = view.findViewById(R.id.deleteButton);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideosActivity.playVideo(v);
                }
            });
        }
    }
}
