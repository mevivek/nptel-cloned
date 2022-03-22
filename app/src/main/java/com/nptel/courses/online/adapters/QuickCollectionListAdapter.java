package com.nptel.courses.online.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nptel.courses.online.R;
import com.nptel.courses.online.databinding.QuickCollectionItemBinding;
import com.nptel.courses.online.entities.Collection;
import com.nptel.courses.online.utility.ColorUtility;

import java.util.List;

public class QuickCollectionListAdapter extends ListAdapter<Collection, QuickCollectionListAdapter.ViewHolder> {

    private MaterialCardView selectedCard;
    private Collection selectedCollection;
    private int[] colors;
    private final String videoId;

    public QuickCollectionListAdapter(String videoId) {
        super(new DiffUtil.ItemCallback<Collection>() {
            @Override
            public boolean areItemsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
        });
        this.videoId = videoId;
    }

    @Override
    public void submitList(@Nullable List<Collection> list) {
        super.submitList(list);
        if (list != null)
            colors = ColorUtility.getRandomColors(150, 150, 150, list.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(QuickCollectionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public Collection getSelectedCollection() {
        return selectedCollection;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final QuickCollectionItemBinding binding;

        ViewHolder(QuickCollectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                if (getItem(getAdapterPosition()).getVideos().contains(videoId)) {
                    Toast.makeText(binding.getRoot().getContext(), "This video is already in this playlist.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCard != null) {
                    selectedCard.setCardElevation(0);
                    selectedCard.findViewById(R.id.selected).setVisibility(View.GONE);
                }
                selectedCard = (MaterialCardView) view;
                selectedCard.setCardElevation(10);
                selectedCard.findViewById(R.id.selected).setVisibility(View.VISIBLE);
                selectedCollection = getItem(getAdapterPosition());
            });

        }

        void bind(Collection collection) {
            binding.alreadyAdded.setVisibility(collection.getVideos().contains(videoId) ? View.VISIBLE : View.GONE);
            binding.setCollection(collection);
            int color = colors[getAdapterPosition()];
            binding.name.setTextColor(color);
            binding.videoCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.alreadyAdded.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color)));
            binding.alreadyAdded.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            MaterialCardView root = (MaterialCardView) binding.getRoot();
            root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(color)));
            binding.selected.setVisibility(selectedCollection != null && selectedCollection.equals(collection) ? View.VISIBLE : View.GONE);
            root.setCardElevation(selectedCollection != null && selectedCollection.equals(collection) ? 10 : 0);
        }
    }
}
