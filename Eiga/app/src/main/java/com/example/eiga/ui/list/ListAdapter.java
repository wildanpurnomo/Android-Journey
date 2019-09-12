package com.example.eiga.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eiga.R;
import com.example.eiga.model.Show;
import com.example.eiga.ui.CustomOnClickListener;
import com.example.eiga.ui.detail.DetailActivity;

import java.util.ArrayList;

import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Show> listShow;
    private int indexHelper;

    public ListAdapter(Activity activity, ArrayList<Show> listShow, int indexHelper) {
        this.activity = activity;
        this.listShow = listShow;
        this.indexHelper = indexHelper;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_col_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindTo(listShow.get(i));
        viewHolder.setOnClickListener(i);
    }

    @Override
    public int getItemCount() {
        return listShow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_col_grid_poster);
            tvTitle = itemView.findViewById(R.id.tv_col_grid_title);
        }

        public void bindTo(Show show) {
            String imgURL = "https://image.tmdb.org/t/p/w500" + show.getPosterPath();
            String title = show.getTitle();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(activity).load(imgURL).apply(options).into(imgPoster);

            tvTitle.setText(title);
        }

        public void setOnClickListener(final int i) {
            itemView.setOnClickListener(new CustomOnClickListener(i, new CustomOnClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    Intent toDetailIntent = new Intent(activity, DetailActivity.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + listShow.get(position).getId());
                    toDetailIntent.setData(uri);
                    toDetailIntent.putExtra(DetailActivity.EXTRA_INDEX, indexHelper);
                    toDetailIntent.putExtra(DetailActivity.EXTRA_ID, listShow.get(i).getId());
                    activity.startActivityForResult(toDetailIntent, DetailActivity.REQUEST_CODE);
                }
            }));
        }
    }
}
