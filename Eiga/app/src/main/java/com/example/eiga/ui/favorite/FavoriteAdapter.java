package com.example.eiga.ui.favorite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements Filterable {
    private ArrayList<Show> listShows = new ArrayList<>();
    private ArrayList<Show> filteredListShows = new ArrayList<>();
    private Activity activity;
    private int indexPage;

    public ArrayList<Show> getListShows() {
        return listShows;
    }

    public void setListShows(ArrayList<Show> listShows) {
        this.listShows = selectionBasedOnIndex(listShows);
        filteredListShows = this.listShows;
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(int indexPage) {
        this.indexPage = indexPage;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_fav, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindTo(filteredListShows.get(i));
        viewHolder.setOnItemClickListener(i);
    }

    @Override
    public int getItemCount() {
        return filteredListShows.size();
    }

    private ArrayList<Show> selectionBasedOnIndex(ArrayList<Show> dirtyShows) {
        ArrayList<Show> cleanShows = new ArrayList<>();

        for (Show item : dirtyShows) {
            if (getIndexPage() == 0) {
                if (item.getType().equals("movie")) {
                    cleanShows.add(item);
                }
            } else {
                if (item.getType().equals("tv")) {
                    cleanShows.add(item);
                }
            }
        }

        return cleanShows;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                if (query.isEmpty()) {
                    filteredListShows = getListShows();
                } else {
                    ArrayList<Show> filteredShows = new ArrayList<>();
                    for (Show item : getListShows()) {
                        if (item.getTitle().toLowerCase().contains(query)) {
                            filteredShows.add(item);
                        }
                    }

                    filteredListShows = filteredShows;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredListShows;

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredListShows = (ArrayList<Show>) results.values;
                notifyDataSetChanged();
            }
        };
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

        public void setOnItemClickListener(final int i) {
            itemView.setOnClickListener(new CustomOnClickListener(i, new CustomOnClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + filteredListShows.get(position).getId());
                    intent.setData(uri);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, position);
                    intent.putExtra(DetailActivity.EXTRA_ID, filteredListShows.get(position).getId());
                    intent.putExtra(DetailActivity.EXTRA_INDEX, filteredListShows.get(position).getType().equals("movie") ? 0 : 1);
                    getActivity().startActivityForResult(intent, DetailActivity.REQUEST_CODE);
                }
            }));
        }
    }
}
