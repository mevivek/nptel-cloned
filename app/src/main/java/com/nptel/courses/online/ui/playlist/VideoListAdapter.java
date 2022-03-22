package com.nptel.courses.online.ui.playlist;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nptel.courses.online.R;
import com.nptel.courses.online.databinding.VideoListItemBinding;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.utility.ColorUtility;
import com.nptel.courses.online.utility.ShimmerViewConverter;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends ListAdapter<Video, RecyclerView.ViewHolder> {

    private VideoListItemBinding currentPlayingItem;
    private int playingVideoPosition;
    private boolean showDelete;

    private int[] colors;

    private final ListItemClickListener<Video> clickListener;

    public VideoListAdapter(ListItemClickListener<Video> clickListener) {
        super(new DiffUtil.ItemCallback<Video>() {

            @Override
            public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return newItem.getYoutubeVideoId().equals(oldItem.getYoutubeVideoId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return newItem.getTitle().equals(oldItem.getTitle()) && newItem.getDescription().equals(oldItem.getDescription());
            }
        });
        playingVideoPosition = -1;
        this.clickListener = clickListener;
        startShimmer();
    }

    @Override
    public void submitList(@Nullable List<Video> list) {
        super.submitList(list);
        if (list != null)
            colors = ColorUtility.getRandomColors(150, 150, 150, list.size());
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getId().contains("dummy") ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new ViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ShimmerViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder)
            ((ViewHolder) holder).bind(getItem(position));
        else ((ShimmerViewHolder) holder).bind(getItem(position));
    }

    public void setShowDelete() {
        showDelete = true;
    }

    public void startShimmer() {
        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            videos.add(Video.getDummy());
        submitList(videos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final VideoListItemBinding binding;

        ViewHolder(final VideoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                setCurrentPlaying();
                currentPlayingItem = binding;
                playingVideoPosition = getAdapterPosition();
                clickListener.onClick(getItem(playingVideoPosition));
            });
            binding.shareVideo.setOnClickListener(view -> clickListener.onOptionClicked(getItem(getAdapterPosition()), view.getId()));
            binding.favourite.setOnClickListener(view -> {
                clickListener.onOptionClicked(getItem(getAdapterPosition()), view.getId());
            });
            binding.addToPlaylist.setOnClickListener(view -> clickListener.onOptionClicked(getItem(getAdapterPosition()), view.getId()));
            binding.deleteVideo.setOnClickListener(view -> clickListener.onOptionClicked(getItem(getAdapterPosition()), view.getId()));
        }

        void bind(Video video) {
            binding.setVideo(video);
            binding.setShowDelete(showDelete);
            if (playingVideoPosition == getAdapterPosition()) {
                binding.playing.setVisibility(View.VISIBLE);
                ((MaterialCardView) binding.getRoot()).setStrokeColor(itemView.getContext().getResources().getColor(R.color.youtube_red_color));
                ((MaterialCardView) binding.getRoot()).setCardElevation(10);
            } else {
                binding.playing.setVisibility(View.GONE);
                int color = getColor(getAdapterPosition());
                binding.line.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"));
                binding.videoTitle.setTextColor(color);
                binding.videoDuration.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
                binding.videoDuration.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));

                binding.coursePlaylist.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
                binding.line2.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"));

                binding.favourite.setImageTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC")));
                binding.addToPlaylist.setImageTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC")));
                binding.shareVideo.setImageTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC")));

                MaterialCardView root = (MaterialCardView) binding.getRoot();
                root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color))));
                root.setStrokeColor(ColorUtility.getLighterVersion(color));
                root.setCardElevation(0);
            }

        }

        private int getColor(int position) {
            if (colors != null && colors.length > position)
                return colors[position];
            colors = ColorUtility.getRandomColors(150, 150, 150, getCurrentList().size(), colors);
            return colors[position];
        }

        private void setCurrentPlaying() {
            if (currentPlayingItem != null) {
                currentPlayingItem.playing.setVisibility(View.GONE);
                ((MaterialCardView) currentPlayingItem.getRoot()).setStrokeColor(itemView.getContext().getResources().getColor(R.color.mildBackgroundStrokeColor));
                ((MaterialCardView) currentPlayingItem.getRoot()).setCardElevation(0);
            }
            binding.playing.setVisibility(View.VISIBLE);
            ((MaterialCardView) binding.getRoot()).setStrokeColor(itemView.getContext().getResources().getColor(R.color.youtube_red_color));
            ((MaterialCardView) binding.getRoot()).setCardElevation(10);
        }
    }

    class ShimmerViewHolder extends RecyclerView.ViewHolder {

        private final VideoListItemBinding binding;

        public ShimmerViewHolder(VideoListItemBinding binding) {
            super(ShimmerViewConverter.wrap(binding.getRoot()));
            this.binding = binding;
        }

        void bind(Video video) {

            binding.setVideo(video);
            binding.executePendingBindings();
            int color = colors[getAdapterPosition()];
            int lighterColor = ColorUtility.getLighterVersion(color, "#36");

            binding.videoThumbnail.setBackgroundColor(lighterColor);
            binding.videoTitle.setTextColor(0);
            binding.videoTitle.setBackgroundColor(lighterColor);

            /*binding.description.setTextColor(0);
            binding.description.setBackgroundColor(lighterColor);*/

            ((MaterialCardView) binding.getRoot()).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)));

            binding.videoDuration.setTextColor(0);
            binding.videoDuration.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));

            binding.favourite.setImageDrawable(null);
            binding.favourite.setBackgroundTintList(ColorStateList.valueOf(lighterColor));

            binding.addToPlaylist.setImageDrawable(null);
            binding.addToPlaylist.setBackgroundTintList(ColorStateList.valueOf(lighterColor));

            binding.shareVideo.setImageDrawable(null);
            binding.shareVideo.setBackgroundTintList(ColorStateList.valueOf(lighterColor));

            binding.deleteVideo.setImageDrawable(null);
            binding.deleteVideo.setBackgroundTintList(ColorStateList.valueOf(lighterColor));
        }
    }
}
