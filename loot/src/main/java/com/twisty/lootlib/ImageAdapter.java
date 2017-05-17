package com.twisty.lootlib;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

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
    private ActionCallback<String> onImageItemClickCallback;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private OnCheckListener onCheckListener;
    Loot loot;


    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    ArrayList<String> getSelectedImages() {
        return selectedImages;
    }


    void setOnImageItemClickCallback(ActionCallback<String> actionCallback) {
        this.onImageItemClickCallback = actionCallback;
    }


    ImageAdapter(List<String> data) {
        this.data = data;
        loot = Loot.getInstance();
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        {
            View view = layoutInflater.inflate(R.layout.loot_image_item, parent, false);
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            float itemWidth = dm.widthPixels / Loot.getInstance().countPerRow;
            layoutParams.width = (int) itemWidth - 2;
            layoutParams.height = (int) itemWidth - 2;
            view.setLayoutParams(layoutParams);
            if (loot.isSingle()) {
                view.findViewById(R.id.checkbox).setVisibility(View.GONE);
            }
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, int position) {
        String imagePath = data.get(position);
        Glide.with(context).load(imagePath).into(imageViewHolder.imageView);
        if (onImageItemClickCallback != null) {
            imageViewHolder.itemView.setOnClickListener(v -> onImageItemClickCallback.onAction(imagePath));
        }
        imageViewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedImages.add(imagePath);
            } else {
                selectedImages.remove(imagePath);
            }
            int maxCount = loot.getMaxCount();
            if (selectedImages.size() > maxCount) {
                imageViewHolder.checkBox.setChecked(!selectedImages.remove(imagePath));
                Toast.makeText(context, "你最多只能选择" + maxCount + "张照片", Toast.LENGTH_SHORT).show();
            }
            if (onCheckListener != null) onCheckListener.onCheck(getSelectedImages().size());
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


    public interface OnCheckListener {
        void onCheck(int selectedItemCount);
    }
}
