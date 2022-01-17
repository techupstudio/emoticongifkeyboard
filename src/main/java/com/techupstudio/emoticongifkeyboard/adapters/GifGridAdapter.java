package com.techupstudio.emoticongifkeyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.model.Gif;

import java.util.List;


public final class GifGridAdapter extends ArrayAdapter<Gif> {
    @NonNull
    private final Context mContext;
    @NonNull
    private final ItemSelectListener mListener;
    @NonNull
    private final List<Gif> mData;

    /**
     * Public constructor.
     *
     * @param context  Instance
     * @param data     List of {@link Gif}
     * @param listener {@link ItemSelectListener} to get callback.
     */
    public GifGridAdapter(@NonNull final Context context,
                          @NonNull final List<Gif> data,
                          @NonNull final ItemSelectListener listener) {
        super(context, R.layout.item_emojicon, data);
        //noinspection ConstantConditions
        if (context == null || data == null || listener == null)
            throw new IllegalArgumentException("Null parameters not allowed.");

        mContext = context;
        mData = data;
        mListener = listener;
    }

    @Override
    public Gif getItem(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_gif, parent, false);

            holder = new ViewHolder();
            holder.gifIv = v.findViewById(R.id.gif_iv);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Gif gif = getItem(position);
        if (gif != null) {
            Glide.with(mContext)
                    .load(gif.getPreviewGifUrl())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.gifIv);

            holder.gifIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnListItemSelected(gif);
                }
            });
        }
        return v;
    }

    /**
     * Callback listener to get notify when item is clicked.
     */
    public interface ItemSelectListener {

        /**
         * @param gif {@link Gif} selected.
         */
        void OnListItemSelected(@NonNull Gif gif);
    }

    /**
     * View holder class to cache views.
     */
    private class ViewHolder {

        /**
         * Image view to display GIFs.
         */
        private ImageView gifIv;
    }
}
