package com.example.remindtetitb.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindtetitb.R;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.Collections;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Info> defaultInfo = new ArrayList<>();
    private ArrayList<Info> filteredListInfo = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Info> getListInfo() {
        return defaultInfo;
    }

    public void setListInfo(ArrayList<Info> listInfo) {
        Collections.reverse(listInfo);
        this.defaultInfo = listInfo;
        filteredListInfo = this.defaultInfo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_placeholder, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindTo(filteredListInfo.get(i));
        viewHolder.setOnClickListener(i);
    }

    @Override
    public int getItemCount() {
        return filteredListInfo.size();
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
                        helperFilter = getListInfo();
                        break;
                    case "kuliah": {
                        for (Info info : getListInfo()) {
                            if (info.getLabel().equals("Perkuliahan")) {
                                helperFilter.add(info);
                            }
                        }
                        break;
                    }
                    case "akademik": {
                        for (Info info : getListInfo()) {
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
                filteredListInfo = (ArrayList<Info>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvLabel;
        private ImageView imgIndicator;

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
                    toDetail.putExtra(DetailActivity.EXTRA_INFO, filteredListInfo.get(i));
                    getContext().startActivity(toDetail);
                }
            });

        }
    }
}
