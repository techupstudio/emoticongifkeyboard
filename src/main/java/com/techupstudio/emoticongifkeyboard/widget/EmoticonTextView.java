package com.techupstudio.emoticongifkeyboard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.util.EmoticonUtils;

public class EmoticonTextView extends AppCompatTextView {
    private int mEmoticonSize;
    @Nullable
    private EmoticonProvider mEmoticonProvider;

    public EmoticonTextView(@NonNull Context context) {
        super(context);
        mEmoticonSize = (int) getTextSize();
        init(null);
    }

    public EmoticonTextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmoticonTextView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable final AttributeSet attrs) {
        if (attrs == null) {
            mEmoticonSize = (int) getTextSize();
        } else {
            @SuppressLint("CustomViewStyleable")
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emoticon);
            mEmoticonSize = (int) a.getDimension(R.styleable.Emoticon_emojiconSize, getTextSize());
            a.recycle();
        }
        setText(getText());
    }

    @Override
    @CallSuper
    public void setText(CharSequence rawText, TextView.BufferType type) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (mEmoticonProvider != null)
            EmoticonUtils.replaceWithImages(getContext(), spannableStringBuilder, mEmoticonProvider, mEmoticonSize);
        super.setText(spannableStringBuilder, type);
    }

    @Override
    @Deprecated
    @CallSuper
    public void append(CharSequence rawText, int start, int end) {
        final String text = getText() + (rawText == null ? "" : rawText).toString();
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (mEmoticonProvider != null)
            EmoticonUtils.replaceWithImages(getContext(), spannableStringBuilder, mEmoticonProvider, mEmoticonSize);
        super.setText(spannableStringBuilder);
    }

    /**
     * Set the size of emojicon in pixels.
     */
    @CallSuper
    public void setEmoticonSize(final int pixels) {
        mEmoticonSize = pixels;
        setText(getText());
    }

    /**
     * Set {@link EmoticonProvider} to display custom emoticon icons.
     *
     * @param emoticonProvider {@link EmoticonProvider} of custom icon packs or null to display
     *                         system icons.
     */
    @CallSuper
    public void setEmoticonProvider(@Nullable final EmoticonProvider emoticonProvider) {
        mEmoticonProvider = emoticonProvider;

        //Refresh the emoticon icons
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        if (mEmoticonProvider != null)
            EmoticonUtils.replaceWithImages(getContext(), spannableStringBuilder, mEmoticonProvider, mEmoticonSize);
    }
}