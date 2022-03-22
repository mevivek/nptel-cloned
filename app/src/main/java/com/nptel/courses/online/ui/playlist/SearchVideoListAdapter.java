package com.nptel.courses.online.ui.playlist;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nptel.courses.online.databinding.SearchVideoItemBinding;
import com.nptel.courses.online.databinding.VideoListItemBinding;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.utility.ColorUtility;
import com.nptel.courses.online.utility.ShimmerViewHolder;

public class SearchVideoListAdapter extends ListAdapter<Video, RecyclerView.ViewHolder> {

    private final ListItemClickListener<Video> clickListener;
    private int[] colors;

    public SearchVideoListAdapter(ListItemClickListener<Video> clickListener) {
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
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new VideoViewHolder(SearchVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ShimmerViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewHolder)
            ((VideoViewHolder) holder).bind(getItem(position));
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private final SearchVideoItemBinding binding;
        private int color;

        VideoViewHolder(final SearchVideoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                clickListener.onClick(getItem(getAdapterPosition()));
            });
        }

        void bind(Video video) {
            binding.setVideo(video);
            color = getColor(getAdapterPosition());

            binding.videoTitle.setTextColor(color);
            binding.videoDuration.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.videoDuration.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));

            binding.coursePlaylist.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
//            binding.coursePlaylist.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"));

            MaterialCardView root = (MaterialCardView) binding.getRoot();
            root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color))));
            root.setStrokeColor(ColorUtility.getLighterVersion(color));
            binding.executePendingBindings();
            for (Video.Highlight highlight : video.getSearch().getHighlights()) {
                highlight(highlight);
            }

        }

        private int getColor(int position) {
            if (colors != null && colors.length > position)
                return colors[position];
            colors = ColorUtility.getRandomColors(150, 150, 150, getCurrentList().size(), colors);
            return colors[position];
        }

        private void highlight(Video.Highlight highlight) {
            int start = -1;
            int end = -1;
            String viewText;
            if (highlight.getPath().equals("title")) {
                for (Video.Highlight.Text text : highlight.getTexts()) {
                    if (text.getType().equals("hit")) {
                        start = binding.videoTitle.getText().toString().indexOf(text.getValue());
                        end = start + text.getValue().length();
                    } else continue;
                    if (start < 0 || end <= start) continue;
                    SpannableString spannedString = new SpannableString(binding.videoTitle.getText());
                    spannedString.setSpan(new BackgroundColorSpan(color), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                    spannedString.setSpan(new ForegroundColorSpan(((MaterialCardView)binding.getRoot()).getCardBackgroundColor().getDefaultColor()), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    binding.videoTitle.setText(spannedString);
                }
            } else if (highlight.getPath().equals("description")) {
                for (Video.Highlight.Text text : highlight.getTexts()) {
                    if (text.getType().equals("hit")) {
                        start = binding.description.getText().toString().indexOf(text.getValue());
                        end = start + text.getValue().length();
                    } else continue;
                    if (start < 0 || end <= start) continue;
                    SpannableString spannedString = new SpannableString(binding.description.getText());
                    spannedString.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                    binding.description.setText(spannedString);
                }
            }
        }
    }
}
