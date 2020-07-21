package com.jiayou.githubsuser;
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
import com.jiayou.githubsuser.model.ItemResponse;

import java.util.List;

public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.ListViewHolder>  {
    private List<ItemResponse> itemResponse;
    private Context context;

    public GithubAdapter(List<ItemResponse> itemResponse, Context context) {
        this.itemResponse = itemResponse;
        this.context = context;
    }

    @NonNull
    @Override
    public GithubAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GithubAdapter.ListViewHolder holder, int position) {
        ItemResponse item = itemResponse.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getAvatarUrl())
                .into(holder.imgPhoto);
        holder.tvName.setText(itemResponse.get(position).getLogin());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemResponse itemm = itemResponse.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("itemm", itemm);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemResponse.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
        }
    }
}
