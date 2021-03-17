package com.project.tmc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tmc.Models.BottomSheet.BottomSheetResult;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResponse;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResult;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.RecycleViewHolder>  {

    ArrayList<BottomSheetResult> data;
    private Context context;
    public BottomSheetAdapter(ArrayList<BottomSheetResult> data, Context context)
    {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public BottomSheetAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_item, parent, false);
        return new BottomSheetAdapter.RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetAdapter.RecycleViewHolder holder, int position)
    {
        holder.sabha_name.setText(data.get(position).getSabhaName());
        holder.sabha_comments.setText(data.get(position).getComment());
        holder.date_time.setText(data.get(position).getCreationTime());
        holder.image_uploaded.setText("Image Uploaded:"+data.get(position).getTotalReceivedImage()+"/"+ data.get(position).getTotalImages());
        holder.image_uploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetImage(data.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder{
        public TextView sabha_name, sabha_comments, date_time, image_uploaded;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            sabha_name = itemView.findViewById(R.id.sabha_name);
            sabha_comments = itemView.findViewById(R.id.sabha_comments);
            date_time = itemView.findViewById(R.id.date_time);
            image_uploaded = itemView.findViewById(R.id.image_uploaded);
        }
    }

    public void imageDialog(ArrayList<BottomSheetImageResult> bottomSheetImageResults) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.image_popup, null);
        ImageView close_image = customView.findViewById(R.id.close_image);
        builder.setView(customView);
        final AlertDialog alert = builder.create();
        alert.setCancelable(true);
        RecyclerView recyclerView = (RecyclerView)customView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BottomSheetImageAdapter(bottomSheetImageResults));
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }

    private void bottomSheetImage(int headerId) {
        if (CommonUtils.isNetworkAvailable(context)) {
            ApplicationActivity.getApiUtility().bottomSheetImage(context, "sabhadetail/Line/"+headerId , true,
                    new APIUtility.APIResponseListener<BottomSheetImageResponse>() {
                        @Override
                        public void onReceiveResponse(BottomSheetImageResponse response) {
                            if (response != null) {
                                imageDialog(response.getResult());

                            } else {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {

                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
        }

    }
}
