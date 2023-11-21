package com.example.infinitypixelcart.Decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing; // Define the spacing value in pixels

    public ItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add spacing to the item's top, left, right, and bottom
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.bottom = spacing;

        // Optionally, you can conditionally apply spacing to certain items
        // For example, to avoid adding spacing to the first item:
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 0;
        } else {
            outRect.top = spacing;
        }
    }
}
