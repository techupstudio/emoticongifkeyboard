package com.techupstudio.emoticongifkeyboard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.util.EmoticonUtils;


public class EmoticonEditText extends AppCompatEditText {
    private static final String TAG = "EmoticonEditText";
    private int mEmoticonSize;
    @Nullable
    private EmoticonProvider mEmoticonProvider;

    public EmoticonEditText(final Context context) {
        super(context);
        mEmoticonSize = (int) getTextSize();
        setText(getText());
    }

    public EmoticonEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmoticonEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable")
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emoticon);
        mEmoticonSize = (int) a.getDimension(R.styleable.Emoticon_emojiconSize, getTextSize());
        a.recycle();

        setText(getText());
    }

    @Override
    @CallSuper
    public void setText(CharSequence rawText, BufferType type) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (mEmoticonProvider != null)
            EmoticonUtils.replaceWithImages(getContext(), spannableStringBuilder, mEmoticonProvider, mEmoticonSize);
        super.setText(text, type);
    }

    @Override
    @CallSuper
    public void append(CharSequence rawText, int start, int end) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText().insert(start, rawText));
        if (mEmoticonProvider != null)
            EmoticonUtils.replaceWithImages(getContext(), spannableStringBuilder, mEmoticonProvider, mEmoticonSize);
        super.setText(spannableStringBuilder);
        setSelection(length());
    }

    /**
     * Remove the last character manually from the text. {@link EmoticonGIFKeyboardFragment}
     * is going to handle backspace manually by itself if the {@link EmoticonEditText} is in focus.
     * You should be implementing this method if you {@link EmoticonEditText} is not in focus and
     * backspace event occurs.
     */
    @CallSuper
    public void backspace() {
        final KeyEvent event = new KeyEvent(0, 0, 0,
                KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                KeyEvent.KEYCODE_ENDCALL);
        dispatchKeyEvent(event);
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
