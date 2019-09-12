package com.example.projectfuelfree.check_history_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.model.Bensin;
import com.example.projectfuelfree.model.Nota;
import com.example.projectfuelfree.purchase_activity.DisplayProductsAdapter;

import java.util.List;

public class CheckHistoryAdapter extends RecyclerView.Adapter<CheckHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Nota> listNota;

    public CheckHistoryAdapter(){

    }

    public CheckHistoryAdapter(Context context, List<Nota> listNota) {
        this.context = context;
        this.listNota = listNota;
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
        Nota nota = listNota.get(position);

        holder.mTvHisProductName.setText(nota.productBought);
        holder.mTvHisDate.setText(nota.dateOfTransaction);
        holder.mTvHisToken.setText("Token : " + nota.token);
        holder.mTvHisNominal.setText("Nominal yang dibeli : " + nota.nominalBought);
        holder.mTvHisLiterGet.setText("Liter yang didapat : " + nota.literGet);

    }

    @Override
    public int getItemCount() {
        return listNota.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvHisProductName, mTvHisDate, mTvHisToken, mTvHisNominal, mTvHisLiterGet;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvHisProductName = itemView.findViewById(R.id.TVhistoryCheckProductBought);
            mTvHisDate = itemView.findViewById(R.id.TVhistoryCheckDateOfTransaction);
            mTvHisToken = itemView.findViewById(R.id.TVhistoryCheckToken);
            mTvHisNominal = itemView.findViewById(R.id.TVhistoryCheckNominalBought);
            mTvHisLiterGet = itemView.findViewById(R.id.TVhistoryCheckLiterGet);
        }
    }
}
