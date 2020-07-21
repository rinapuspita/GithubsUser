package com.jiayou.githubsuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jiayou.githubsuser.DetailActivity;
import com.jiayou.githubsuser.R;
import com.jiayou.githubsuser.model.ItemResponse;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>{
    private ArrayList<ItemResponse> itemResponse;
    private Context context;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<ItemResponse> getItemResponse() {
        return itemResponse;
    }

    public void setItemResponse(ArrayList<ItemResponse> itemResponse) {
        this.itemResponse = itemResponse;
    }

    @NonNull
    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.MyViewHolder holder, int position) {
        ItemResponse item = itemResponse.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getAvatarUrl())
                .into(holder.imgPhoto);
        holder.tvName.setText(itemResponse.get(position).getLogin());
        int id = item.getId();
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemResponse itemm = itemResponse.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("itemm", itemm);
                intent.putExtra("name", itemResponse.get(position).getLogin());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemResponse.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
        }
    }
}
