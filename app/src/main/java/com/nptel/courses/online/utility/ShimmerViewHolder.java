package com.nptel.courses.online.utility;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ShimmerViewHolder extends RecyclerView.ViewHolder {

    public ShimmerViewHolder(ViewDataBinding binding) {
        super(ShimmerViewConverter.convert(binding.getRoot()));
    }
}