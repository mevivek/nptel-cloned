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
import com.nptel.courses.online.databinding.CourseItemBinding;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.utility.ColorUtility;
import com.nptel.courses.online.utility.OnClickListener;
import com.nptel.courses.online.utility.ShimmerViewConverter;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends ListAdapter<Course, RecyclerView.ViewHolder> {

    private final OnClickListener<Course> listener;
    private int[] colors;

    public CourseListAdapter(OnClickListener<Course> listener) {
        super(new DiffUtil.ItemCallback<Course>() {

            @Override
            public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
                return newItem.getId().equals(oldItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
                return newItem.getTitle().equals(oldItem.getTitle());
            }
        });
        this.listener = listener;
        startShimmer();
    }

    @Override
    public void submitList(@Nullable List<Course> list) {
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
            return new ViewHolder(CourseItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ShimmerViewHolder(CourseItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder)
            ((ViewHolder) holder).bind(getItem(position));
        else ((ShimmerViewHolder) holder).bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final CourseItemBinding binding;

        ViewHolder(CourseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> listener.onClick(getItem(getAdapterPosition())));
        }

        void bind(Course course) {
            binding.setCourse(course);
            int color = colors[getAdapterPosition()];
            binding.initial.setTextColor(color);

            int lighterColor = ColorUtility.getLighterVersion(color, "#36");

            binding.initial.setBackgroundTintList(ColorStateList.valueOf(lighterColor));
            binding.name.setTextColor(color);
            ((MaterialCardView) binding.getRoot()).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)));

            binding.playlistsCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.playlistsCountProgress.setIndicatorColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.playlistsCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));
            binding.videosCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.videosCountProgress.setIndicatorColor(ColorUtility.getLighterVersion(color, "#80"));
            binding.videosCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));
        }

    }

    class ShimmerViewHolder extends RecyclerView.ViewHolder {

        private CourseItemBinding binding;

        public ShimmerViewHolder(CourseItemBinding binding) {
            super(ShimmerViewConverter.wrap(binding.getRoot()));
            this.binding = binding;
        }

        void bind(Course course) {

            binding.setCourse(course);
            int color = colors[getAdapterPosition()];
            binding.initial.setTextColor(0);
            int lighterColor = ColorUtility.getLighterVersion(color, "#36");
            binding.initial.setBackgroundTintList(ColorStateList.valueOf(lighterColor));
            binding.name.setTextColor(0);
            binding.name.setBackgroundColor(lighterColor);

            ((MaterialCardView) binding.getRoot()).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)));

            binding.playlistsCount.setTextColor(0);
            binding.playlistsCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));

            binding.videosCount.setTextColor(0);
            binding.videosCountContainer.setBackgroundTintList(ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A")));
        }
    }

    public void startShimmer() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            courses.add(Course.getDummy());
        submitList(courses);
    }
}
