package com.example.remindtetitb.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.remindtetitb.R;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.detail.DetailActivity;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Info> searchedInfo = new ArrayList<>();
    private ArrayList<Info> filteredSearch = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Info> getSearchedInfo() {
        return searchedInfo;
    }

    public void setSearchedInfo(ArrayList<Info> searchedInfo) {
        this.searchedInfo = searchedInfo;
        filteredSearch = this.searchedInfo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_placeholder, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindTo(getSearchedInfo().get(i));
        viewHolder.setOnClickListener(i);
    }

    @Override
    public int getItemCount() {
        return filteredSearch.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filter = constraint.toString().toLowerCase();
                ArrayList<Info> helperFilter = new ArrayList<>();

                switch (filter) {
                    case "all":
                        helperFilter = getSearchedInfo();
                        break;
                    case "kuliah": {
                        for (Info info : getSearchedInfo()) {
                            if (info.getLabel().equals("Perkuliahan")) {
                                helperFilter.add(info);
                            }
                        }
                        break;
                    }
                    case "akademik": {
                        for (Info info : getSearchedInfo()) {
                            if (info.getLabel().equals("Akademik")) {
                                helperFilter.add(info);
                            }
                        }
                        break;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = helperFilter;

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSearch = (ArrayList<Info>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvLabel;

        Resources resources = context.getResources();
        Drawable bgAkademik = ResourcesCompat.getDrawable(resources, R.drawable.bg_tag_akademik, null);
        Drawable bgKuliah = ResourcesCompat.getDrawable(resources, R.drawable.bg_tag_kuliah, null);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_description);
            tvLabel = itemView.findViewById(R.id.tv_item_infotag);
        }

        public void bindTo(Info info){
            tvTitle.setText(info.getTitle());
            tvContent.setText(info.getContent());

            tvLabel.setText(info.getLabel());
            if(info.getLabel().equals("Perkuliahan")){
                tvLabel.setBackground(bgKuliah);
            } else if (info.getLabel().equals("Akademik")){
                tvLabel.setBackground(bgAkademik);
            }
        }

        public void setOnClickListener(final int i){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toDetail = new Intent(getContext(), DetailActivity.class);
                    toDetail.putExtra(DetailActivity.EXTRA_INFO, filteredSearch.get(i));
                    getContext().startActivity(toDetail);
                }
            });

        }
    }
}
