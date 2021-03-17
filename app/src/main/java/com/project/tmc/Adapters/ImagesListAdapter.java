package com.project.tmc.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tmc.R;

import java.io.File;
import java.util.ArrayList;

public class ImagesListAdapter extends RecyclerView.Adapter<ImagesListAdapter.RecycleViewHolder> {

    ArrayList<File> data = new ArrayList<>();
    ArrayList<Boolean> imageCheck = new ArrayList<>();
    private boolean hideDeleteButton;
    private boolean successFail;

    public ImagesListAdapter(ArrayList<File> data, ArrayList<Boolean> imageCheck, boolean hideDeleteButton, boolean successFail) {
        this.data = data;
        this.imageCheck = imageCheck;
        this.hideDeleteButton = hideDeleteButton;
        this.successFail = successFail;
    }

    @NonNull
    @Override
    public ImagesListAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item_layout, parent, false);
        return new ImagesListAdapter.RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesListAdapter.RecycleViewHolder holder, int position) {
        if (hideDeleteButton) {
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
        }

        if (successFail) {
            holder.successFailsImage.setVisibility(View.VISIBLE);
        } else {
            holder.successFailsImage.setVisibility(View.GONE);
        }
        if (imageCheck != null) {
            if (imageCheck.get(position)) {
                holder.successFailsImage.setImageResource(R.drawable.sucess);
            } else {
                holder.successFailsImage.setImageResource(R.drawable.fail);
            }
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.imageView.setImageURI(Uri.fromFile(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, successFailsImage, delete;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            successFailsImage = itemView.findViewById(R.id.successFailsImage);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
