package com.project.tmc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.project.tmc.Models.BottomSheet.BottomSheetResult;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResult;
import com.project.tmc.R;
import java.util.ArrayList;


public class BottomSheetImageAdapter extends RecyclerView.Adapter<BottomSheetImageAdapter.RecycleViewHolder>  {

    ArrayList<BottomSheetImageResult> data;
    private Context context;
    public BottomSheetImageAdapter(ArrayList<BottomSheetImageResult> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public BottomSheetImageAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_pop_layout, parent, false);
        context = parent.getContext();
        return new BottomSheetImageAdapter.RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetImageAdapter.RecycleViewHolder holder, int position)
    {

        Glide.with(context).load(data.get(position).getImageUrl()).into(holder.imageView);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
