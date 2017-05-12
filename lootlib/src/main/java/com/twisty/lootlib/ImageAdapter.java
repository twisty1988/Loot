package com.twisty.lootlib;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/12.<br>
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<String> data;
    private Context context;
    private ActionListener<String> actionListener;
    private List<String> selectedImages = new ArrayList<>();

    public List<String> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public void setActionListener(ActionListener<String> actionListener) {
        this.actionListener = actionListener;
    }

    public ImageAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.loot_image_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float itemWidth = dm.widthPixels / 4;
        layoutParams.width = (int) itemWidth - 3;
        layoutParams.height = (int) itemWidth - 3;
        view.setLayoutParams(layoutParams);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imagePath = data.get(position);
        Glide.with(context).load(imagePath).into(holder.imageView);
        if (actionListener != null) {
            holder.itemView.setOnClickListener(v -> actionListener.onAction(imagePath));
        }
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedImages.add(imagePath);
            } else {
                selectedImages.remove(imagePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
