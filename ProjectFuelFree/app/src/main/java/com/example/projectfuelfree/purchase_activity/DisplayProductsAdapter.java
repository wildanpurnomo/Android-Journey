package com.example.projectfuelfree.purchase_activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.model.Bensin;

import java.util.ArrayList;
import java.util.List;

public class DisplayProductsAdapter extends RecyclerView.Adapter<DisplayProductsAdapter.ViewHolder> {
    private Context context;
    private List<Bensin> listBensin;

    public DisplayProductsAdapter(){

    }

    public DisplayProductsAdapter(Context context, List<Bensin> listBensin) {
        this.context = context;
        this.listBensin = listBensin;
    }

    @NonNull
    @Override
    public DisplayProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_products, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnclickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String chosenProduct = viewHolder.mTvProductName.getText().toString();
                Intent i = new Intent(context, PurchaseProductActivity.class);
                i.putExtra(PurchaseProductActivity.EXTRA_PRODUCT_NAME, chosenProduct);

                context.startActivity(i);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayProductsAdapter.ViewHolder holder, int position) {
        Bensin bensin = listBensin.get(position);

        holder.mTvProductName.setText(bensin.name);
        holder.mTvProductPrice.setText("Rp " + bensin.pricePerLiter + ",- / liter");
    }

    @Override
    public int getItemCount() {
        return listBensin.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvProductName, mTvProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvProductName = itemView.findViewById(R.id.TVproductNameDisplayer);
            mTvProductPrice = itemView.findViewById(R.id.TVproductPriceDIsplayer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        private ViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        public void setOnclickListener(ViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }


}
