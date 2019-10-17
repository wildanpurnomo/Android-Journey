package com.example.remindtetitb.ui.custom;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class InfoDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public InfoDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildLayoutPosition(view) == state.getItemCount() - 1){
            outRect.bottom = 0;
        } else{
            outRect.bottom = spacing;
        }
    }
}
