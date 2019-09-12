package com.example.dzakiyh.simpel.check_history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.model.FormulirSIMBaru;

import java.util.List;

public class CheckHistoryAdapter extends RecyclerView.Adapter<CheckHistoryAdapter.ViewHolder> {

    private Context context;
    private List<FormulirSIMBaru> listForm;

    public CheckHistoryAdapter(){

    }

    public CheckHistoryAdapter(Context context, List<FormulirSIMBaru>listForm){
        this.context = context;
        this.listForm = listForm;
    }


    @NonNull
    @Override
    public CheckHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_history, parent, false);

        CheckHistoryAdapter.ViewHolder viewHolder = new CheckHistoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckHistoryAdapter.ViewHolder holder, int position) {

        FormulirSIMBaru form =listForm.get(position);

        holder.mTVTanggal.setText(form.getTanggalKirim());
        holder.mTVJenisFormulir.setText(form.getJenisFormulir());

    }

    @Override
    public int getItemCount() {
        return listForm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTVTanggal, mTVJenisFormulir;
        public ViewHolder(View itemView) {
            super(itemView);

            mTVTanggal = itemView.findViewById(R.id.TV_history_tanggal);
            mTVJenisFormulir = itemView.findViewById(R.id.TV_history_jenisForm);
        }
    }
}
