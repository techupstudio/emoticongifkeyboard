package com.techupstudio.emoticongifkeyboard.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.adapters.EmoticonGridAdapter;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonSelectListener;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonsCategories;
import com.techupstudio.emoticongifkeyboard.core.model.Emoticon;
import com.techupstudio.emoticongifkeyboard.database.EmoticonDbHelper;
import com.techupstudio.emoticongifkeyboard.shared.RecentEmoticonManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public final class EmoticonGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_CATEGORY_ID = "arg_category_id";

    /**
     * Listener to notify when emoticons selected.
     */
    @Nullable
    private EmoticonSelectListener mEmoticonSelectListener;

    /**
     * Emoticon provider
     */
    @Nullable
    private EmoticonProvider mEmoticonProvider;


    public EmoticonGridFragment() {
        // Required empty public constructor
    }

    /**
     * Get the new instance of {@link EmoticonGridFragment}.
     *
     * @param category               Category id to display.
     * @param emoticonProvider       {@link EmoticonProvider} to customize the emoticon
     * @param emoticonSelectListener {@link EmoticonSelectListener} to notify when {@link Emoticon}
     *                               gets selected.
     * @return new {@link EmoticonGridFragment}.
     */
    static EmoticonGridFragment newInstance(@EmoticonsCategories.EmoticonsCategory int category,
                                            @Nullable EmoticonSelectListener emoticonSelectListener,
                                            @Nullable EmoticonProvider emoticonProvider) {
        EmoticonGridFragment emoticonGridFragment = new EmoticonGridFragment();
        emoticonGridFragment.setEmoticonProvider(emoticonProvider);
        emoticonGridFragment.setEmoticonSelectListener(emoticonSelectListener);

        //Set the category id arguments
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, category);
        emoticonGridFragment.setArguments(bundle);

        return emoticonGridFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoticon_grid, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Emoticon> emoticonList = getEmoticonsList(requireArguments().getInt(ARG_CATEGORY_ID, -1));
        ViewFlipper mainViewFlipper = requireView().findViewById(R.id.recent_emoticon_flipper);
        if (emoticonList.isEmpty()) {
            mainViewFlipper.setDisplayedChild(1);
        } else {
            mainViewFlipper.setDisplayedChild(0);

            GridView emoticonGrid = view.findViewById(R.id.emoticon_list_grid);
            emoticonGrid.setNumColumns(getResources().getInteger(R.integer.emoticon_recycler_view_span_size));
            emoticonGrid.setOnItemClickListener(this);

            EmoticonGridAdapter emoticonGridAdapter = new EmoticonGridAdapter(requireActivity(),
                    mEmoticonProvider, emoticonList);
            emoticonGrid.setAdapter(emoticonGridAdapter);

            if (getArguments() != null && requireArguments().getInt(ARG_CATEGORY_ID, -1) == EmoticonsCategories.RECENT) {
                RecentEmoticonManager.getInstance(requireContext()).setRecentEmoticonListChangedListener(emoticonArrayList -> {
                    emoticonGridAdapter.setEmoticons(emoticonArrayList);
                    emoticonGridAdapter.notifyDataSetChanged();
                });
            }
        }
    }

    /**
     * Set the {@link EmoticonSelectListener} to get notify whenever the emoticon is selected or deleted.
     *
     * @param emoticonSelectListener {@link EmoticonSelectListener}
     */
    @SuppressWarnings("ConstantConditions")
    public void setEmoticonSelectListener(@Nullable EmoticonSelectListener emoticonSelectListener) {
        mEmoticonSelectListener = emoticonSelectListener;
    }

    /**
     * Set the {@link EmoticonProvider} to render different images for unicode. If the value is null,
     * system emoticon images will render.
     *
     * @param emoticonProvider {@link EmoticonProvider}
     */
    public void setEmoticonProvider(@Nullable EmoticonProvider emoticonProvider) {
        mEmoticonProvider = emoticonProvider;
    }

    /**
     * Get the emoticons list for the selected category.
     *
     * @param category category id.
     * @return List of {@link Emoticon}
     */
    private List<Emoticon> getEmoticonsList(@EmoticonsCategories.EmoticonsCategory int category) {
        switch (category) {
            case EmoticonsCategories.RECENT:
                return RecentEmoticonManager.getInstance(getActivity()).getRecentEmoticons();
            case EmoticonsCategories.PEOPLE:
            case EmoticonsCategories.NATURE:
            case EmoticonsCategories.FOOD:
            case EmoticonsCategories.ACTIVITY:
            case EmoticonsCategories.TRAVEL:
            case EmoticonsCategories.OBJECTS:
            case EmoticonsCategories.SYMBOLS:
            case EmoticonsCategories.FLAGS:
                return new EmoticonDbHelper(requireActivity()).getEmoticons(category, mEmoticonProvider);
            default:
                throw new IllegalStateException("Invalid position.");
        }
    }

    /**
     * When any of the item from the emoticon grid will click, this method will call.
     *
     * @param position Position if the item clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Emoticon emoticon = (Emoticon) adapterView.getAdapter().getItem(position);

        //Notify the emoticon
        if (mEmoticonSelectListener != null)
            mEmoticonSelectListener.emoticonSelected(emoticon);

        //Save the emoticon to the recent list
        if (getArguments() != null && requireArguments().getInt(ARG_CATEGORY_ID, -1) != EmoticonsCategories.RECENT) {
            RecentEmoticonManager.getInstance(getActivity()).add(emoticon);
        }
    }
}
