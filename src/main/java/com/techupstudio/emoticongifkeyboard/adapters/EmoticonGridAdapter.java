package com.techupstudio.emoticongifkeyboard.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.core.model.Emoticon;
import com.techupstudio.emoticongifkeyboard.core.model.EmoticonSpan;

import java.util.List;

/**
 * Created by Keval on 01-Sep-17.
 * An adapter to bind the {@link Emoticon} list with the grid view.
 *
 * @author <a href='https://github.com/kevalpatel2106'>Kevalpatel2106</a>
 */

public final class EmoticonGridAdapter extends BaseAdapter {
    /**
     * Instance of caller.
     */
    @NonNull
    private final Context mContext;

    /**
     * {@link EmoticonProvider} to provide the custom icon. If the this field is null system
     * emoticon icons will render.
     */
    @Nullable
    private final EmoticonProvider mEmoticonProvider;

    /**
     * List of the {@link Emoticon} to display.
     */
    @NonNull
    private List<Emoticon> mEmoticons;

    /**
     * Package private constructor.
     *
     * @param context          Instance of the caller.
     * @param emoticonProvider {@link EmoticonProvider} to provide the custom icon. If the this field is null system
     *                         emoticon icons will render.
     * @param emoticons        List of {@link Emoticon} to display.
     */
    public EmoticonGridAdapter(@NonNull Context context,
                               @Nullable EmoticonProvider emoticonProvider,
                               @NonNull List<Emoticon> emoticons) {
        mContext = context;
        mEmoticonProvider = emoticonProvider;
        mEmoticons = emoticons;
    }

    @Override
    public int getCount() {
        return mEmoticons.size();
    }

    @Override
    public Emoticon getItem(int index) {
        return mEmoticons.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emojicon,
                    viewGroup,
                    false);

        Emoticon emoticon = getItem(position);
        if (emoticon != null) {
            if (mEmoticonProvider != null
                    && mEmoticonProvider.hasEmoticonIcon(emoticon.getUnicode())) { //Check if the icon for this emoticon is available?

                //Convert to spannable.
                // Replace the emoticon image with provided by custom icon pack.
                Spannable spannable = new SpannableString(emoticon.getUnicode());
                spannable.setSpan(new EmoticonSpan(mContext,
                                mEmoticonProvider.getIcon(emoticon.getUnicode()),
                                mContext.getResources().getDimension(R.dimen.emoticon_grid_text_size)),
                        0,
                        spannable.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                ((TextView) convertView).setText(spannable);
            } else {
                ((TextView) convertView).setText(emoticon.getUnicode());
            }
        }
        return convertView;
    }

    @NonNull
    public List<Emoticon> getEmoticons() {
        return mEmoticons;
    }

    public void setEmoticons(@NonNull List<Emoticon> mEmoticons) {
        this.mEmoticons = mEmoticons;
    }
}
