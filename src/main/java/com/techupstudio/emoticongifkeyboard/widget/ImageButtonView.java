package com.techupstudio.emoticongifkeyboard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.util.ColorUtils;

/**
 * Created by Keval on 19-Aug-17.
 * Custom {@link AppCompatImageView} to change the tint for the buttons when selected or click.
 * This class is for internal use only.
 *
 * @author <a href='https://github.com/kevalpatel2106'>Kevalpatel2106</a>
 */

public final class ImageButtonView extends AppCompatImageView {

    @ColorInt
    private int mIconPressedInColor;

    @ColorInt
    private int mIconPressedOutColor;

    public ImageButtonView(@NonNull Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageButtonView(@NonNull Context context) {
        super(context, null);
        init(context);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setColorFilter(selected ? mIconPressedOutColor : mIconPressedInColor, PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(@NonNull Context context) {
        mIconPressedOutColor = getAccentColor(context);
        mIconPressedInColor = ColorUtils.getDarkColor(mIconPressedOutColor);
        //Set on touch listener to change the tint color when image is pressed.
        setOnTouchListener((view, motionEvent) -> {
            ImageButtonView.this.setColorFilter(motionEvent.getAction() == MotionEvent.ACTION_DOWN ?
                    getIconPressedInColor() : getIconPressedOutColor(), PorterDuff.Mode.SRC_ATOP);
            return false;
        });
        //Set the icon color to accent color
        setColorFilter(mIconPressedOutColor, PorterDuff.Mode.SRC_ATOP);
    }

    public int getIconPressedInColor() {
        return mIconPressedInColor;
    }

    public void setIconPressedInColor(@ColorInt int iconPressInColor) {
        mIconPressedInColor = iconPressInColor;
    }

    public int getIconPressedOutColor() {
        return mIconPressedOutColor;
    }

    public void setIconPressedOutColor(@ColorInt int iconPressOutColor) {
        mIconPressedOutColor = iconPressOutColor;
        setColorFilter(mIconPressedOutColor, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Extract the accent color from the application theme.
     *
     * @param context {@link Context}
     * @return Accent color.
     */
    @ColorInt
    public int getAccentColor(@NonNull final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorAccent, value, true);
        return value.data;
    }
}
