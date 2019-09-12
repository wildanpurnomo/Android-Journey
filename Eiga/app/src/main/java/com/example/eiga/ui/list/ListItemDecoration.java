package com.example.eiga.ui.list;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public ListItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildLayoutPosition(view) == state.getItemCount() - 1) {
            outRect.right = 0;
        } else {
            outRect.right = spacing;
        }
    }
}
