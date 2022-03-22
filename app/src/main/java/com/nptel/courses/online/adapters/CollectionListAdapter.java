package com.nptel.courses.online.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nptel.courses.online.databinding.CollectionBinding;
import com.nptel.courses.online.entities.Collection;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.utility.ColorUtility;

import java.util.List;

public class CollectionListAdapter extends ListAdapter<Collection, CollectionListAdapter.PlaylistViewHolder> {

    private final ListItemClickListener<Collection> clickListener;
    private int[] colors;

    public CollectionListAdapter(ListItemClickListener<Collection> clickListener) {
        super(new DiffUtil.ItemCallback<Collection>() {
            @Override
            public boolean areItemsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
        });
        this.clickListener = clickListener;
    }

    @Override
    public void submitList(@Nullable List<Collection> list) {
        super.submitList(list);
        if (list != null)
            colors = ColorUtility.getRandomColors(150, 150, 150, list.size());
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaylistViewHolder(CollectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private final CollectionBinding binding;

        PlaylistViewHolder(CollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                clickListener.onClick(getItem(getAdapterPosition()));
            });
            binding.edit.setOnClickListener(view -> clickListener.onOptionClicked(getItem(getAdapterPosition()), view.getId()));

        }

        void bind(Collection collection) {
            binding.setCollection(collection);
            int color = colors[getAdapterPosition()];
            binding.name.setTextColor(color);
            binding.videoCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.edit.setImageTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#80")));
            MaterialCardView root = (MaterialCardView) binding.getRoot();
            root.setCardBackgroundColor(ColorUtility.getLighterVersion(color));
            root.setStrokeColor(ColorUtility.getLighterVersion(color, "#33"));
        }
    }
}
