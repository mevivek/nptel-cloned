package com.nptel.courses.online.ui.main;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nptel.courses.online.databinding.PlaylistBinding;
import com.nptel.courses.online.entities.Playlist;
import com.nptel.courses.online.utility.ColorUtility;
import com.nptel.courses.online.utility.OnClickListener;
import com.nptel.courses.online.utility.ShimmerViewConverter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsAdapter extends ListAdapter<Playlist, RecyclerView.ViewHolder> {

    private final OnClickListener<Playlist> listener;
    private int[] colors;

    public PlaylistsAdapter(OnClickListener<Playlist> listener) {
        super(new DiffUtil.ItemCallback<Playlist>() {

            @Override
            public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
                return newItem.getId().equals(oldItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
                return newItem.getTitle().equals(oldItem.getTitle()) && newItem.getDescription().equals(oldItem.getDescription());
            }
        });
        this.listener = listener;
        startShimmer();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getId().contains("dummy") ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new ViewHolder(PlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ShimmerViewHolder(PlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder)
            ((ViewHolder) holder).bind(getItem(position));
        else ((ShimmerViewHolder) holder).bind(getItem(position));
    }

    @Override
    public void submitList(@Nullable List<Playlist> list) {
        super.submitList(list);
        if (list != null)
            colors = ColorUtility.getRandomColors(150, 150, 150, list.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final PlaylistBinding binding;

        ViewHolder(PlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> listener.onClick(getItem(getAdapterPosition())));
        }

        void bind(Playlist playlist) {
            binding.setPlaylist(playlist);
            int color = colors[getAdapterPosition()];
            binding.title.setTextColor(color);
            binding.description.setTextColor(ColorUtility.getLighterVersion(color, "#B3"));
            binding.videosCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.videosCountProgress.setIndicatorColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.videosCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));
            binding.line.setBackgroundColor(ColorUtility.getLighterVersion(color));
            MaterialCardView root = (MaterialCardView) binding.getRoot();
            root.setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)));
            root.setStrokeColor(ColorUtility.getLighterVersion(color));
        }
    }

    class ShimmerViewHolder extends RecyclerView.ViewHolder {

        private final PlaylistBinding binding;

        public ShimmerViewHolder(PlaylistBinding binding) {
            super(ShimmerViewConverter.wrap(binding.getRoot()));
            this.binding = binding;
        }

        void bind(Playlist playlist) {

            binding.setPlaylist(playlist);
            int color = colors[getAdapterPosition()];
            int lighterColor = ColorUtility.getLighterVersion(color, "#36");
            binding.moduleImage.setBackgroundColor(lighterColor);
            binding.title.setTextColor(0);
            binding.title.setBackgroundColor(lighterColor);

            binding.description.setTextColor(0);
            binding.description.setBackgroundColor(lighterColor);

            ((MaterialCardView) binding.getRoot()).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)));

            binding.videosCount.setTextColor(0);
            binding.videosCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));
        }
    }

    public void startShimmer() {
        List<Playlist> playlists = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            playlists.add(Playlist.getDummy());
        submitList(playlists);
    }
}
