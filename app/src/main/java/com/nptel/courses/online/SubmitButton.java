package com.nptel.courses.online;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;

/**
 * This button view will be used where a network call is performed after button click and
 * we need to show a progress bar on the button.
 */
public class SubmitButton extends MaterialButton {
    private AnimatedVectorDrawable animatedVectorDrawable;
    private Drawable defaultIcon;
    private int defaultIconGravity;

    public SubmitButton(Context context) {
        super(context);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        animatedVectorDrawable = (AnimatedVectorDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.circular_progress, getContext().getTheme());
        defaultIcon = getIcon();
        defaultIconGravity = getIconGravity();
        if (isInEditMode()) {
            setText(getText() == null ? "Submit": getText());
        }
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SubmitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, R.style.SubmitButtonStyle);
        init(attrs, defStyle);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setEnabled(enabled, false);
    }

    public void showProgress() {
        setEnabled(false, true);
    }

    public void hideProgress() {
        setEnabled(true);
    }

    private void setEnabled(boolean enable, boolean showProgress) {
        super.setEnabled(enable);
        if (animatedVectorDrawable == null) {
            return;
        }
        if (showProgress) {
            setIcon(animatedVectorDrawable);
            setIconGravity(ICON_GRAVITY_TEXT_END);
            animatedVectorDrawable.start();
            return;
        }
        setIcon(defaultIcon);
        setIconGravity(defaultIconGravity);
        animatedVectorDrawable.stop();
    }
}
