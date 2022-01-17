package com.techupstudio.emoticongifkeyboard.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonSelectListener;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonsCategories;
import com.techupstudio.emoticongifkeyboard.shared.RecentEmoticonManager;
import com.techupstudio.emoticongifkeyboard.widget.ImageButtonView;

import org.jetbrains.annotations.NotNull;


/**
 * A {@link Fragment} subclass to load the list of emoticons and display them based on the categories.
 *
 * @author 'https://github.com/kevalpatel2106'
 */
public final class EmoticonFragment extends Fragment {
    /***
     * Total number of tabs in emoticons.
     */
    private static final int TOTAL_TABS = 9;

    /**
     * Instance of caller.
     */
    private Context mContext;

    /**
     * Listener to notify when emoticons selected.
     */
    @Nullable
    private EmoticonSelectListener mEmoticonSelectListener;

    /**
     * Recently used emoticons.
     */
    private RecentEmoticonManager mRecentEmoticonManager;

    /**
     * Emoticon provider
     */
    @Nullable
    private EmoticonProvider mEmoticonProvider;

    private ViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;
    private ImageButtonView[] mTabIcons;


    public EmoticonFragment() {
        // Required empty public constructor
    }

    /**
     * Get the new instance of {@link EmoticonFragment}. Use this method over calling constructor.
     * This function is for internal use only.
     *
     * @return {@link EmoticonFragment}
     */
    public static EmoticonFragment newInstance(@Nullable Bundle args) {
        EmoticonFragment fragment = new EmoticonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecentEmoticonManager = RecentEmoticonManager.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emoticon, container, false);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.emoji_category_view_pager);
        mViewPager.setAdapter(new EmoticonCategoryViewpagerAdapter(getParentFragmentManager()));

        if (getArguments() != null) {
            view.findViewById(R.id.emoji_tabs).setBackgroundColor(requireArguments().getInt("headerFooterColor"));
            ((SmartTabLayout) view.findViewById(R.id.emoji_tabs)).setDividerColors(requireArguments().getInt("tabDividerColor"));
            view.findViewById(R.id.content_container).setBackgroundColor(requireArguments().getInt("bodyColor"));
        }

        mSmartTabLayout = view.findViewById(R.id.emoji_tabs);
        mTabIcons = new ImageButtonView[9];

        mSmartTabLayout.setCustomTabView((container, position, adapter) -> {
            ImageButtonView icon = new ImageButtonView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            icon.setLayoutParams(params);
//            icon.setBackgroundColor(getResources().getColor(R.color.rsc_emoji_tab_bkg));
//            icon.setScaleType(ImageView.ScaleType.CENTER);
            int DP_8 = dpToPx(8);
            icon.setPadding(DP_8, DP_8, DP_8, DP_8);
            switch (position) {
                case EmoticonsCategories.RECENT:
                    icon.setImageResource(R.drawable.emoji_recent);
                    break;
                case EmoticonsCategories.PEOPLE:
                    icon.setImageResource(R.drawable.emoji_people);
                    break;
                case EmoticonsCategories.NATURE:
                    icon.setImageResource(R.drawable.emoji_nature);
                    break;
                case EmoticonsCategories.FOOD:
                    icon.setImageResource(R.drawable.emoji_food);
                    break;
                case EmoticonsCategories.ACTIVITY:
                    icon.setImageResource(R.drawable.emoji_activity);
                    break;
                case EmoticonsCategories.TRAVEL:
                    icon.setImageResource(R.drawable.emoji_travel);
                    break;
                case EmoticonsCategories.OBJECTS:
                    icon.setImageResource(R.drawable.emoji_objects);
                    break;
                case EmoticonsCategories.SYMBOLS:
                    icon.setImageResource(R.drawable.emoji_symbols);
                    break;
                case EmoticonsCategories.FLAGS:
                    icon.setImageResource(R.drawable.emoji_flags);
                    break;
            }

            if (getArguments() != null) {
                icon.setIconPressedInColor(requireArguments().getInt("iconPressInColor"));
                icon.setIconPressedOutColor(requireArguments().getInt("iconPressOutColor"));
            }

            mTabIcons[position] = icon;
            mTabIcons[position].setOnClickListener(v -> mViewPager.setCurrentItem(position));
            return icon;
        });

        mSmartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int lastPosition = EmoticonFragment.this.mRecentEmoticonManager.getLastCategory();

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRecentEmoticonManager.setLastCategory(position);
                mTabIcons[lastPosition].setSelected(false);
                mTabIcons[position].setSelected(true);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onStart() {
        //Select recent tabs selected while creating new instance
        try {
            EmoticonFragment.this.mViewPager.post(() -> {
                EmoticonFragment.this.mViewPager.setAdapter(new EmoticonCategoryViewpagerAdapter(getChildFragmentManager()));
                EmoticonFragment.this.mViewPager.setCurrentItem(EmoticonFragment.this.mRecentEmoticonManager.getLastCategory());
                EmoticonFragment.this.mSmartTabLayout.setViewPager(EmoticonFragment.this.mViewPager);
                EmoticonFragment.this.mTabIcons[EmoticonFragment.this.mRecentEmoticonManager.getLastCategory()].setSelected(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
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


    private int dpToPx(double dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private class EmoticonCategoryViewpagerAdapter extends FragmentStatePagerAdapter {

        public EmoticonCategoryViewpagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return TOTAL_TABS;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return EmoticonGridFragment.newInstance(position,   //Position. Same as emoticon category id
                    mEmoticonSelectListener,    //Callback listener
                    mEmoticonProvider);         //Emoticon icon provider;
        }
    }

}
