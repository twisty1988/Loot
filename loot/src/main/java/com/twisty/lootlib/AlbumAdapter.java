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

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Album> data;
    private ActionCallback<Album> actionCallback;
    private ActionCallback<String> onCameraClickCallback;

    public static final int ITEM_TYPE_CAMERA = 1, ITEM_TYPE_ALBUM = 2;

    public void setOnCameraClickCallback(ActionCallback<String> onCameraClickCallback) {
        this.onCameraClickCallback = onCameraClickCallback;
    }

    public void setActionCallback(ActionCallback<Album> actionCallback) {
        this.actionCallback = actionCallback;
    }

    public AlbumAdapter(List<Album> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (Loot.getInstance().isHasCamera() && position == 0) {
            return ITEM_TYPE_CAMERA;
        } else {
            return ITEM_TYPE_ALBUM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_CAMERA) {
            View view = inflater.inflate(R.layout.camera_item, parent, false);
            return new CameraViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.loot_album_item, parent, false);
            return new AlbumViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_CAMERA) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            if (onCameraClickCallback != null) {
                cameraViewHolder.itemView.setOnClickListener(v -> onCameraClickCallback.onAction(null));
            }
        } else {
            AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
            Album album = Loot.getInstance().isHasCamera() ? data.get(position - 1) : data.get(position);
            albumViewHolder.nameView.setText(album.getAlbumName());
            albumViewHolder.imageCountView.setText(String.format(Locale.CHINA, "(%d)", album.getImageCount()));
            Glide.with(context).load(album.getLatestImagePath()).into(albumViewHolder.latestImageView);
            if (actionCallback != null) {
                holder.itemView.setOnClickListener(v -> actionCallback.onAction(album));
            }
        }

    }

    @Override
    public int getItemCount() {
        return Loot.getInstance().isHasCamera() ? data.size() + 1 : data.size();
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

    static class CameraViewHolder extends RecyclerView.ViewHolder {
        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
