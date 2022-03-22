package com.nptel.courses.online.interfaces;

import androidx.annotation.IdRes;

public interface ListItemClickListener<T> {

    void onClick(T t);

    void onOptionClicked(T t, @IdRes int optionId);
}
