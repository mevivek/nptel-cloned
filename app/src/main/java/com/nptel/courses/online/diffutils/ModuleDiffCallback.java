package com.nptel.courses.online.diffutils;

import com.nptel.courses.online.entities.Playlist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ModuleDiffCallback extends DiffUtil.ItemCallback<Playlist> {

    @Override
    public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
        return newItem.getId().equals(oldItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
        return newItem.getTitle().equals(oldItem.getTitle()) && newItem.getDescription().equals(oldItem.getDescription());
    }
}
