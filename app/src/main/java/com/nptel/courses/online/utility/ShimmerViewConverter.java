package com.nptel.courses.online.utility;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.nptel.courses.online.R;

public class ShimmerViewConverter {

    public static View wrap(View view) {
        ShimmerFrameLayout shimmerFrameLayout = new ShimmerFrameLayout(view.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(view.getLayoutParams());

        shimmerFrameLayout.setLayoutParams(layoutParams);

        shimmerFrameLayout.addView(view);
        shimmerFrameLayout.startShimmer();
        return shimmerFrameLayout;
    }

    public static View convert(View view) {

        ShimmerFrameLayout shimmerFrameLayout = new ShimmerFrameLayout(view.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(view.getLayoutParams());

        shimmerFrameLayout.setLayoutParams(layoutParams);
        if (view instanceof ViewGroup) setBackground((ViewGroup) view);
        else
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.mildBackgroundColor));
        shimmerFrameLayout.addView(view);
        shimmerFrameLayout.startShimmer();
        return shimmerFrameLayout;
    }

    static void setBackground(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++)
            if (view.getChildAt(i) instanceof ViewGroup)
                setBackground((ViewGroup) view.getChildAt(i));
            else
                view.getChildAt(i).setBackgroundColor(view.getContext().getResources().getColor(R.color.mildBackgroundColor));

    }
}