package com.techupstudio.emoticongifkeyboard.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.core.model.Emoticon;
import com.techupstudio.emoticongifkeyboard.core.model.EmoticonSpan;

import java.util.List;

/**
 * Created by Keval on 18-Aug-17.
 * Adapter to bind the list of {@link Emoticon} to recycler view.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public final class EmoticonSearchAdapter extends RecyclerView.Adapter<EmoticonSearchAdapter.ViewHolder> {

    /**
     * Context
     */
    @NonNull
    private final Context mContext;

    /**
     * List of {@link Emoticon} to display.
     */
    @NonNull
    private final List<Emoticon> mData;

    /**
     * {@link EmoticonProvider} to display custom emoticon icons.
     */
    @Nullable
    private final EmoticonProvider mEmoticonProvider;

    /**
     * {@link ItemSelectListener} to get callbacks when any emoticon gets select.
     */
    @NonNull
    private final ItemSelectListener mListener;

    /**
     * Public constructor.
     *
     * @param context          Instance of caller.
     * @param data             List of {@link Emoticon} to display.
     * @param emoticonProvider {@link EmoticonProvider} to display custom emoticon icons. To display
     *                         system emoticons pass null.
     * @param listener         {@link ItemSelectListener} to get callbacks when any
     *                         emoticon gets select.
     */
    public EmoticonSearchAdapter(@NonNull Context context,
                                 @NonNull List<Emoticon> data,
                                 @Nullable EmoticonProvider emoticonProvider,
                                 @NonNull ItemSelectListener listener) {
        //Validate inputs
        //noinspection ConstantConditions
        if (context == null || data == null || listener == null)
            throw new IllegalArgumentException("Null parameters not allowed.");

        mContext = context;
        mData = data;
        mEmoticonProvider = emoticonProvider;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_emojicon, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Emoticon emoji = mData.get(position);
        if (emoji != null) {
            if (mEmoticonProvider != null
                    && mEmoticonProvider.hasEmoticonIcon(emoji.getUnicode())) { //Check if the icon for this emoticon is available?

                //Convert to spannable.
                // Replace the emoticon image with provided by custom icon pack.
                Spannable spannable = new SpannableString(emoji.getUnicode());
                spannable.setSpan(new EmoticonSpan(mContext, mEmoticonProvider.getIcon(emoji.getUnicode()), mContext.getResources().getDimension(R.dimen.emoticon_grid_text_size)),
                        0,
                        spannable.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.icon.setText(spannable);
            } else {
                holder.icon.setText(emoji.getUnicode());
            }
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnEmoticonSelected(emoji);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Callback listener to get notify when item is clicked.
     */
    public interface ItemSelectListener {
        /**
         * @param emoticon {@link Emoticon} selected.
         */
        void OnEmoticonSelected(@NonNull Emoticon emoticon);
    }

    /**
     * View holder class.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView icon;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.emojicon_icon);
        }
    }
}
