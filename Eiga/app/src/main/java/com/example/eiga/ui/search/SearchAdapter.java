package com.example.eiga.ui.search;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Show> listSearch;
    private int indexHelper;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Show> getListSearch() {
        return listSearch;
    }

    public void setListSearch(ArrayList<Show> listSearch) {
        this.listSearch = listSearch;
        notifyDataSetChanged();
    }

    public int getIndexHelper() {
        return indexHelper;
    }

    public void setIndexHelper(int indexHelper) {
        this.indexHelper = indexHelper;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_list_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindTo(getListSearch().get(i));
        viewHolder.setOnClickListener(i);
    }

    @Override
    public int getItemCount() {
        return listSearch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView tvTitle, tvType, tvRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_search_poster);
            tvTitle = itemView.findViewById(R.id.tv_search_title);
            tvType = itemView.findViewById(R.id.tv_search_type);
            tvRate = itemView.findViewById(R.id.tv_search_rate);
        }

        public void bindTo(Show show) {
            if (show != null) {
                String imgURL = "https://image.tmdb.org/t/p/w500" + show.getPosterPath();
                String title = show.getTitle();
                String type = show.getType();
                Double rate = show.getVoteAverage();

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);
                Glide.with(activity).load(imgURL).apply(options).into(imgPoster);

                tvTitle.setText(title);
                tvType.setText(type);
                tvRate.setText(String.valueOf(rate));
            }
        }

        public void setOnClickListener(int position) {
            itemView.setOnClickListener(new CustomOnClickListener(position, new CustomOnClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    int indexHelper = getIndexHelper();
                    long id = getListSearch().get(position).getId();
                    Uri uri = Uri.parse(CONTENT_URI + "/" + id);

                    Intent moveToDetailIntent = new Intent(activity, DetailActivity.class);
                    moveToDetailIntent.setData(uri);
                    moveToDetailIntent.putExtra(DetailActivity.EXTRA_INDEX, indexHelper);
                    moveToDetailIntent.putExtra(DetailActivity.EXTRA_ID, id);
                    getActivity().startActivityForResult(moveToDetailIntent, DetailActivity.REQUEST_CODE);
                }
            }));
        }
    }


}
