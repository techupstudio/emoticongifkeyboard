package com.techupstudio.emoticongifkeyboard.util;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorUtils {

    @ColorInt
    public static int getDarkColor(final int color) {
        final float factor = 0.6f;

        final int a = Color.alpha(color);
        final int r = Math.round(Color.red(color) * factor);
        final int g = Math.round(Color.green(color) * factor);
        final int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

}
