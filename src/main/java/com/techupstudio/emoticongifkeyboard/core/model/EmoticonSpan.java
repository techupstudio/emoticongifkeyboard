package com.techupstudio.emoticongifkeyboard.core.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import androidx.appcompat.content.res.AppCompatResources;


public final class EmoticonSpan extends DynamicDrawableSpan {
    private final float mEmoticonSize;
    private final Context mContext;
    private final int mEmoticonIcon;
    private Drawable mDeferredDrawable;

    public EmoticonSpan(final Context context, final int emoticonIcon, final float size) {
        this.mContext = context;
        this.mEmoticonIcon = emoticonIcon;
        this.mEmoticonSize = size;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Drawable getDrawable() {
        if (mDeferredDrawable == null) {
            mDeferredDrawable = AppCompatResources.getDrawable(mContext, mEmoticonIcon);
            mDeferredDrawable.setBounds(0, 0, (int) mEmoticonSize, (int) mEmoticonSize);
        }
        return mDeferredDrawable;
    }

    @Override
    public int getSize(final Paint paint, final CharSequence text, final int start,
                       final int end, final Paint.FontMetricsInt fontMetrics) {
        if (fontMetrics != null) {
            final Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
            final float fontHeight = paintFontMetrics.descent - paintFontMetrics.ascent;
            final float centerY = paintFontMetrics.ascent + fontHeight / 2;

            fontMetrics.ascent = (int) (centerY - mEmoticonSize / 2);
            fontMetrics.top = fontMetrics.ascent;
            fontMetrics.bottom = (int) (centerY + mEmoticonSize / 2);
            fontMetrics.descent = fontMetrics.bottom;
        }

        return (int) mEmoticonSize;
    }

    @Override
    public void draw(final Canvas canvas, final CharSequence text, final int start,
                     final int end, final float x, final int top, final int y,
                     final int bottom, final Paint paint) {
        final Drawable drawable = getDrawable();
        final Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        final float fontHeight = paintFontMetrics.descent - paintFontMetrics.ascent;
        final float centerY = y + paintFontMetrics.descent - fontHeight / 2;
        final float transitionY = centerY - mEmoticonSize / 2;

        canvas.save();
        canvas.translate(x, transitionY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
