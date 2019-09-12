package com.example.simpleconsumerapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.simpleconsumerapp.Model.Show;
import com.example.simpleconsumerapp.R;

import java.util.ArrayList;

public class ConsumerAdapter extends RecyclerView.Adapter<ConsumerAdapter.ViewHolder> {
    private final ArrayList<Show> listShows = new ArrayList<>();
    private final Activity activity;

    public ConsumerAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Show> getListShows() {
        return listShows;
    }

    public void setListShows(ArrayList<Show> listShows) {
        this.listShows.clear();
        this.listShows.addAll(listShows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindTo(getListShows().get(i));
    }

    @Override
    public int getItemCount() {
        return getListShows().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPoster;
        final TextView tvTitle, tvCategory, tvRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_fav_poster);
            tvTitle = itemView.findViewById(R.id.tv_fav_title);
            tvCategory = itemView.findViewById(R.id.tv_fav_type);
            tvRate = itemView.findViewById(R.id.tv_fav_rate);
        }

        public void bindTo(Show show) {
            String imgURL = "https://image.tmdb.org/t/p/w500" + show.getPosterPath();
            String title = show.getTitle();
            String type = show.getType();
            Double rate = show.getVoteAverage();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(activity.getApplicationContext()).load(imgURL).apply(options).into(imgPoster);

            tvTitle.setText(title);
            tvCategory.setText(type);
            tvRate.setText(String.valueOf(rate));
        }
    }
}
