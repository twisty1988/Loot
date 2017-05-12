package com.twisty.lootlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/12.<br>
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    Context context;
    List<Album> data;
    private ActionListener<Album> actionListener;

    public void setActionListener(ActionListener<Album> actionListener) {
        this.actionListener = actionListener;
    }

    public AlbumAdapter(List<Album> data) {
        this.data = data;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loot_album_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = data.get(position);
        holder.nameView.setText(album.getAlbumName());
        holder.imageCountView.setText(String.format(Locale.CHINA, "(%d)", album.getImageCount()));
        Glide.with(context).load(album.getLatestImagePath()).into(holder.latestImageView);
        if (actionListener != null) {
            holder.itemView.setOnClickListener(v -> actionListener.onAction(album));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, imageCountView;
        private ImageView latestImageView;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.albumName);
            imageCountView = (TextView) itemView.findViewById(R.id.imageCount);
            latestImageView = (ImageView) itemView.findViewById(R.id.latestImage);
        }
    }
}
