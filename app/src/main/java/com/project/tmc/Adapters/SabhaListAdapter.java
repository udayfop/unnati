package com.project.tmc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tmc.Models.sabhaList.SabhaListResult;
import com.project.tmc.R;
import com.project.tmc.activities.ImageCommentActivity;

import java.util.ArrayList;


public class SabhaListAdapter extends RecyclerView.Adapter<SabhaListAdapter.RecycleViewHolder>{

    private ArrayList<SabhaListResult> data;
    private Context context;
    public SabhaListAdapter(ArrayList<SabhaListResult> data ) {
        this.data = data;
    }

    @NonNull
    @Override
    public SabhaListAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sabha_list_layout, parent, false);
        context = parent.getContext();
        return new SabhaListAdapter.RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SabhaListAdapter.RecycleViewHolder holder, int position)
    {
        holder.list_sabha_name.setText(String.valueOf(data.get(position).getSabhaName()));
        holder.list_sabha_description.setText(String.valueOf(data.get(position).getDescription()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity
                        (new Intent(context, ImageCommentActivity.class).putExtra("sabhaName", data.get(position).getSabhaName())
                        .putExtra("sabhaId", data.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class RecycleViewHolder extends RecyclerView.ViewHolder{
        private TextView list_sabha_name,list_sabha_description;
        private CardView card;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            list_sabha_name =itemView.findViewById(R.id.list_sabha_name);
            list_sabha_description = itemView.findViewById(R.id.list_sabha_description);

            card = (CardView)itemView.findViewById(R.id.card);
        }
    }

    public void updateList(ArrayList<SabhaListResult> list){
        data = list;
        notifyDataSetChanged();
    }
    public interface SabhaDataInterface{
        void partyData(SabhaListResult sabhaListResult);
    }
}
