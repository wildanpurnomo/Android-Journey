package com.example.remindtetitb.ui.main;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.remindtetitb.R;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

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

                switch (filter) {
                    case "all":
                        filteredListInfo = getListInfo();
                        break;
                    case "kuliah": {
                        ArrayList<Info> helperFilter = new ArrayList<>();
                        for (Info info : getListInfo()) {
                            if (info.getLabel().equals("Perkuliahan")) {
                                helperFilter.add(info);
                            }
                        }

                        filteredListInfo = helperFilter;
                        break;
                    }
                    case "akademik": {
                        ArrayList<Info> helperFilter = new ArrayList<>();
                        for (Info info : getListInfo()) {
                            if (info.getLabel().equals("Akademik")) {
                                helperFilter.add(info);
                            }
                        }

                        filteredListInfo = helperFilter;
                        break;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredListInfo;

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
        Drawable imgAdded = ResourcesCompat.getDrawable(resources, R.drawable.item_added, null);
        Drawable imgNotAdded = ResourcesCompat.getDrawable(resources, R.drawable.item_not_added, null);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_description);
            tvLabel = itemView.findViewById(R.id.tv_item_infotag);
            imgIndicator = itemView.findViewById(R.id.img_item_add_indicator);
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

            Glide.with(context).load(imgNotAdded).override(100, 100).into(imgIndicator);
        }
    }
}
