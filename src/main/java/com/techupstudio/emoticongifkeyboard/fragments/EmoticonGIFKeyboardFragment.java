package com.techupstudio.emoticongifkeyboard.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.techupstudio.emoticongifkeyboard.R;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonProvider;
import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonSelectListener;
import com.techupstudio.emoticongifkeyboard.core.interfaces.GifProviderProtocol;
import com.techupstudio.emoticongifkeyboard.core.interfaces.GifSelectListener;
import com.techupstudio.emoticongifkeyboard.widget.ImageButtonView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;


/**
 * This {@link Fragment} will host the smiles and gifs.
 * Add this fragment into your view of activity or fragment.
 *
 * @author 'https://github.com/kevalpatel2106'
 */
public final class EmoticonGIFKeyboardFragment extends Fragment {
    /**
     * Tags for the fragment back stack
     */
    public static final String TAG_EMOTICON_FRAGMENT = "tag_emoticon_fragment";
    public static final String TAG_GIF_FRAGMENT = "tag_gif_fragment";
    public static final String TAG_EMOTICON_SEARCH_FRAGMENT = "tag_emoticon_search_fragment";
    public static final String TAG_GIF_SEARCH_FRAGMENT = "tag_gif_search_fragment";

    /**
     * Key for saved instance bundle.
     */
    private static final String KEY_CURRENT_FRAGMENT = "current_fragment";

    /**
     * Fragments to load.
     */
    @Nullable
    private EmoticonFragment mEmoticonFragment;
    @Nullable
    private EmoticonSearchFragment mEmoticonSearchFragment;
    @Nullable
    private GifFragment mGifFragment;
    @Nullable
    private GifSearchFragment mGifSearchFragment;

    /**
     * Listener to notify when emoticons selected.
     */
    @Nullable
    private EmoticonSelectListener mEmoticonSelectListener;
    @Nullable
    private GifSelectListener mGifSelectListener;
    @Nullable
    private GifProviderProtocol mGifProvider;
    @Nullable
    private EmoticonProvider mEmoticonProvider;

    /**
     * View container that hosts search, backspace and tabs buttons.
     */
    private View mBottomViewContainer;
    private View mGifTabBtn;
    private View mEmoticonTabBtn;
    private View mBackSpaceBtn;
    private View mRootView;

    /**
     * Bool to indicate weather emoticon functionality is enabled or not?
     */
    private boolean mIsEmoticonsEnable;

    /**
     * Bool to indicate weather GIF functionality is enabled or not?
     */
    private boolean mIsGIFsEnable;

    /**
     * Bool to indicate if the keyboard is currently open/
     */
    private boolean mIsOpen = true;
    private String mLastFragmentTag;
    private String mCurrentFragmentTag;
    private OnBackPressedCallback mOnBackPressedCallback;


    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public EmoticonGIFKeyboardFragment() {

    }

    /**
     * Get the new instance of {@link EmoticonGIFKeyboardFragment}. Call this method to get repairable
     * instance of fragment instead of directly calling constructor. Set the emoticon and GIF configurations
     * in this function. You can also disable either emoticon or GIF functionality by passing
     * {@link EmoticonConfig} null or {@link GIFConfig} null respectively. At least one functionality
     * should be non null.
     *
     * @param container      Container view that encloses the {@link EmoticonGIFKeyboardFragment}.
     * @param emoticonConfig {@link EmoticonConfig} with emoticon configurations. If this parameter
     *                       is null emoticon listing and emoticon search functionality will be disabled.
     * @param gifConfig      {@link GIFConfig} with GIF configurations. If this parameter
     *                       is null GIF listing and GIF search functionality will be disabled.
     * @return {@link EmoticonGIFKeyboardFragment}
     */
    @SuppressWarnings("deprecation")
    public static EmoticonGIFKeyboardFragment newInstance(@NonNull View container,
                                                          @Nullable EmoticonConfig emoticonConfig,
                                                          @Nullable GIFConfig gifConfig) {

        //Validate inputs
        if (emoticonConfig == null && gifConfig == null)
            throw new IllegalStateException("At least one of emoticon or GIF should be active.");

        //Set the layout parameter for container
        container.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        container.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        //Initialize the fragment
        EmoticonGIFKeyboardFragment emoticonGIFKeyboardFragment = new EmoticonGIFKeyboardFragment();
        emoticonGIFKeyboardFragment.setRetainInstance(true);

        //Set emoticons
        if (emoticonConfig != null) {
            emoticonGIFKeyboardFragment.mIsEmoticonsEnable = true;
            emoticonGIFKeyboardFragment.setEmoticonProvider(emoticonConfig.mEmoticonProvider);
            emoticonGIFKeyboardFragment.setEmoticonSelectListener(emoticonConfig.mEmoticonSelectListener);
        }

        //Set GIFs
        if (gifConfig != null) {
            emoticonGIFKeyboardFragment.mIsGIFsEnable = true;
            emoticonGIFKeyboardFragment.setGifProvider(gifConfig.mGifProviderProtocol);
            emoticonGIFKeyboardFragment.setGifSelectListener(gifConfig.mGifSelectListener);
        }

        Bundle args = new Bundle();
        if (emoticonConfig != null) {
            Bundle emoticonColorArgs = new Bundle();
            emoticonColorArgs.putInt("headerFooterColor", emoticonConfig.getHeaderFooterColor());
            emoticonColorArgs.putInt("bodyColor", emoticonConfig.getBodyColor());
            emoticonColorArgs.putInt("iconPressInColor", emoticonConfig.getIconPressInColor());
            emoticonColorArgs.putInt("iconPressOutColor", emoticonConfig.getIconPressOutColor());
            emoticonColorArgs.putInt("tabDividerColor", emoticonConfig.getTabDividerColor());
            args.putBundle("emoticonConfig", emoticonColorArgs);
        }

        if (gifConfig != null) {
            Bundle gifColorArgs = new Bundle();
            gifColorArgs.putInt("headerFooterColor", gifConfig.getHeaderFooterColor());
            gifColorArgs.putInt("bodyColor", gifConfig.getBodyColor());
            gifColorArgs.putInt("iconPressInColor", gifConfig.getIconPressInColor());
            gifColorArgs.putInt("iconPressOutColor", gifConfig.getIconPressOutColor());
            args.putBundle("gifConfig", gifColorArgs);
        }

        emoticonGIFKeyboardFragment.setArguments(args);

        return emoticonGIFKeyboardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoticon_gif_keyboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view.findViewById(R.id.root_view);

        mOnBackPressedCallback = mOnBackPressedCallback != null ? mOnBackPressedCallback : new OnBackPressedCallback(true) {

            final FragmentActivity activity = requireActivity();

            @Override
            public void handleOnBackPressed() {
                activity.onBackPressed();
            }
        };

        if (requireArguments().containsKey("emoticonConfig")) {
            requireView().findViewById(R.id.bottom_container).setBackgroundColor(requireArguments().getBundle("emoticonConfig").getInt("headerFooterColor"));
            for (int i : new int[]{R.id.search_btn, R.id.emojis_backspace, R.id.btn_emoji_tab, R.id.btn_gif_tab}) {
                ((ImageButtonView) requireView().findViewById(i)).setIconPressedInColor(requireArguments().getBundle("emoticonConfig").getInt("iconPressInColor"));
                ((ImageButtonView) requireView().findViewById(i)).setIconPressedOutColor(requireArguments().getBundle("emoticonConfig").getInt("iconPressOutColor"));
            }
        }

        if (requireArguments().containsKey("gifConfig")) {
//            mGifFragment.setArguments(requireArguments().getBundle("gifConfig"));
//            mGifSearchFragment.setArguments(requireArguments().getBundle("gifConfig"));
        }

        mBottomViewContainer = view.findViewById(R.id.bottom_container);

        //Set backspace button
        mBackSpaceBtn = view.findViewById(R.id.emojis_backspace);
        mBackSpaceBtn.setOnClickListener(view1 -> {
            if (mEmoticonSelectListener != null) mEmoticonSelectListener.onBackSpace();
        });

        //Set emoticon button
        mEmoticonTabBtn = view.findViewById(R.id.btn_emoji_tab);
        mEmoticonTabBtn.setOnClickListener(view12 -> switchFragment(TAG_EMOTICON_FRAGMENT));
        mEmoticonTabBtn.setVisibility(isEmoticonsEnabled() && isGIFsEnabled() ? View.VISIBLE : View.GONE);

        //Set GIF button
        mGifTabBtn = view.findViewById(R.id.btn_gif_tab);
        mGifTabBtn.setOnClickListener(view13 -> switchFragment(TAG_GIF_FRAGMENT));
        mGifTabBtn.setVisibility(isEmoticonsEnabled() && isGIFsEnabled() ? View.VISIBLE : View.GONE);

        view.findViewById(R.id.search_btn).setOnClickListener(view14 -> {
            if (isGIFsEnabled() && mGifTabBtn.isSelected()) {
                switchFragment(TAG_GIF_SEARCH_FRAGMENT);
            } else {
                switchFragment(TAG_EMOTICON_SEARCH_FRAGMENT);
            }
        });

        if (savedInstanceState != null) {   //Fragment reloaded from config changes,
            switchFragment(savedInstanceState.getString(KEY_CURRENT_FRAGMENT));
        } else {
            //Display emoticon fragment by default.
//            Utility.log("onCreate");
            if (isEmoticonsEnabled())
                switchFragment(TAG_EMOTICON_FRAGMENT);
            else if (isGIFsEnabled())
                switchFragment(TAG_GIF_FRAGMENT);
            else
                throw new IllegalStateException("At least one of emoticon or GIF should be active.");
        }
    }

    private void switchFragment(String tag) {
        switch (tag) {
            case TAG_GIF_FRAGMENT:
                replaceFragment(mGifFragment = getGifFragment(), TAG_GIF_FRAGMENT);
                break;
            case TAG_EMOTICON_FRAGMENT:
                replaceFragment(mEmoticonFragment = getEmoticonFragment(), TAG_EMOTICON_FRAGMENT);
                break;
            case TAG_EMOTICON_SEARCH_FRAGMENT:
                replaceFragment(mEmoticonSearchFragment = getEmoticonSearchFragment(), TAG_EMOTICON_SEARCH_FRAGMENT);
                break;
            case TAG_GIF_SEARCH_FRAGMENT:
                replaceFragment(mGifSearchFragment = getGifSearchFragment(), TAG_GIF_SEARCH_FRAGMENT);
                break;
        }
    }

    @NonNull
    private EmoticonFragment getEmoticonFragment() {
        EmoticonFragment emoticonFragment = EmoticonFragment.newInstance(null);
        if (requireArguments().containsKey("emoticonConfig")) {
            emoticonFragment.setArguments(requireArguments().getBundle("emoticonConfig"));
        }
        emoticonFragment.setEmoticonProvider(mEmoticonProvider);
        emoticonFragment.setEmoticonSelectListener(mEmoticonSelectListener);
        return emoticonFragment;
    }

    @NonNull
    private GifFragment getGifFragment() {
        GifFragment gifFragment = GifFragment.newInstance(null);
        if (requireArguments().containsKey("gifConfig")) {
            gifFragment.setArguments(requireArguments().getBundle("gifConfig"));
        }
        gifFragment.setGifProvider(Objects.requireNonNull(mGifProvider));
        gifFragment.setGifSelectListener(mGifSelectListener);
        return gifFragment;
    }

    @NonNull
    private GifSearchFragment getGifSearchFragment() {
        GifSearchFragment gifSearchFragment = GifSearchFragment.newInstance(null);
        if (requireArguments().containsKey("gifConfig")) {
            gifSearchFragment.setArguments(requireArguments().getBundle("gifConfig"));
            gifSearchFragment.setOnBackIconPressedCallback(mOnBackPressedCallback);
        }
        gifSearchFragment.setGifProvider(Objects.requireNonNull(mGifProvider));
        gifSearchFragment.setGifSelectListener(mGifSelectListener);
        return gifSearchFragment;
    }

    @NonNull
    private EmoticonSearchFragment getEmoticonSearchFragment() {
        EmoticonSearchFragment emoticonSearchFragment = EmoticonSearchFragment.newInstance(null);
        if (requireArguments().containsKey("emoticonConfig")) {
            emoticonSearchFragment.setArguments(requireArguments().getBundle("emoticonConfig"));
            emoticonSearchFragment.setOnBackIconPressedCallback(mOnBackPressedCallback);
        }
        emoticonSearchFragment.setEmoticonProvider(mEmoticonProvider);
        emoticonSearchFragment.setEmoticonSelectListener(Objects.requireNonNull(mEmoticonSelectListener));
        return emoticonSearchFragment;
    }

    /**
     * Replace the fragment in the fragment container.
     *
     * @param fragment New {@link Fragment} to replace
     * @param tag      Tag for the back stack entry
     */
    private void replaceFragment(@NonNull Fragment fragment, @FragmentBackStackTags String tag) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.keyboard_fragment_container, fragment, mLastFragmentTag = tag)
                .commit();
        changeLayoutFromTag(tag);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save current fragment
        outState.putString(KEY_CURRENT_FRAGMENT, getCurrentFragmentTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_FRAGMENT)) {
            switchFragment(savedInstanceState.getString(KEY_CURRENT_FRAGMENT));
        }
    }

    public String getLastFragmentTag() {
        return mLastFragmentTag;
    }

    public String getCurrentFragmentTag() {
        return mCurrentFragmentTag;
    }

    /**
     * Make layout changes based on the fragment tag.
     *
     * @param tag Fragment tags.
     * @see FragmentBackStackTags
     */
    private void changeLayoutFromTag(@FragmentBackStackTags String tag) {
        switch (mCurrentFragmentTag = tag) {
            case TAG_EMOTICON_FRAGMENT:
                //Display bottom bar of not displayed.
                if (mBottomViewContainer != null) mBottomViewContainer.setVisibility(View.VISIBLE);
                mGifTabBtn.setSelected(false);
                mEmoticonTabBtn.setSelected(true);
                mGifTabBtn.setSelected(!mEmoticonTabBtn.isSelected());
                mBackSpaceBtn.setVisibility(View.VISIBLE);
                break;
            case TAG_GIF_FRAGMENT:
                //Display bottom bar of not displayed.
                if (mBottomViewContainer != null) mBottomViewContainer.setVisibility(View.VISIBLE);
                mGifTabBtn.setSelected(true);
                mEmoticonTabBtn.setSelected(false);
                mGifTabBtn.setSelected(!mEmoticonTabBtn.isSelected());
                mBackSpaceBtn.setVisibility(View.GONE);
                break;
            case TAG_EMOTICON_SEARCH_FRAGMENT:
            case TAG_GIF_SEARCH_FRAGMENT:
                //Display bottom bar of not displayed.
                if (mBottomViewContainer != null) mBottomViewContainer.setVisibility(View.GONE);
                break;
            default:
                //Do nothing
                break;
        }
    }


    /**
     * Set the {@link EmoticonSelectListener} to get notify whenever the emoticon is selected or deleted.
     *
     * @param emoticonSelectListener {@link EmoticonSelectListener}
     * @see EmoticonSelectListener
     */
    @SuppressWarnings("ConstantConditions")
    public void setEmoticonSelectListener(@Nullable EmoticonSelectListener emoticonSelectListener) {
        mEmoticonSelectListener = emoticonSelectListener;
        if (mEmoticonFragment != null) mEmoticonFragment.setEmoticonSelectListener(emoticonSelectListener);
        if (mEmoticonSearchFragment != null) mEmoticonSearchFragment.setEmoticonSelectListener(emoticonSelectListener);
    }

    /**
     * Set the GIF provider for for fetching the GIFs.
     *
     * @param gifProvider Loader class that extends {@link GifProviderProtocol}.
     * @see GifProviderProtocol
     */
    private void setGifProvider(@NonNull GifProviderProtocol gifProvider) {
        mGifProvider = gifProvider;
        if (mGifFragment != null) mGifFragment.setGifProvider(gifProvider);
        if (mGifSearchFragment != null) mGifSearchFragment.setGifProvider(gifProvider);
    }

    /**
     * Set the {@link EmoticonProvider} to render different images for unicode. If the value is null,
     * system emoticon images will render.
     *
     * @param emoticonProvider {@link EmoticonProvider} for custom emoticon packs or null to use system
     *                         emoticons.
     * @see EmoticonProvider
     */
    public void setEmoticonProvider(@Nullable EmoticonProvider emoticonProvider) {
        mEmoticonProvider = emoticonProvider;
        if (mEmoticonFragment != null) mEmoticonFragment.setEmoticonProvider(emoticonProvider);
        if (mEmoticonSearchFragment != null) mEmoticonSearchFragment.setEmoticonProvider(emoticonProvider);
    }

    /**
     * Set the {@link EmoticonSelectListener} to get notify whenever the emoticon is selected or deleted.
     *
     * @param gifSelectListener {@link EmoticonSelectListener}
     * @see GifSelectListener
     */
    public void setGifSelectListener(@Nullable GifSelectListener gifSelectListener) {
        mGifSelectListener = gifSelectListener;
        if (mGifFragment != null) mGifFragment.setGifSelectListener(gifSelectListener);
        if (mGifSearchFragment != null) mGifSearchFragment.setGifSelectListener(gifSelectListener);
    }

    public void setOnBackPressedCallback(OnBackPressedCallback onBackPressedCallback) {
        this.mOnBackPressedCallback = onBackPressedCallback;
    }

    public OnBackPressedCallback getOnBackPressedCallback() {
        return mOnBackPressedCallback;
    }

    /**
     * @return True if emoticons are enable for the keyboard.
     */
    public boolean isEmoticonsEnabled() {
        return mIsEmoticonsEnable;
    }

    /**
     * @return True if GIFs are enable for the keyboard.
     */
    public boolean isGIFsEnabled() {
        return mIsGIFsEnable;
    }

    public boolean isSearchEnabled() {
        return mCurrentFragmentTag != null && mCurrentFragmentTag.contains("search");
    }

    /**
     * Show thw keyboard with resize animation.
     */
    public synchronized void open() {
        if (mRootView != null)
            mRootView.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the keyboard with resize animation.
     */
    public synchronized void close() {
        if (mRootView != null)
            mRootView.setVisibility(View.GONE);
        switchFragment(TAG_EMOTICON_FRAGMENT);
    }

    /**
     * Check if the {@link EmoticonGIFKeyboardFragment} is open/visible?
     *
     * @return True if {@link EmoticonGIFKeyboardFragment} is visible.
     */
    public boolean isOpen() {
        return mRootView.getVisibility() == View.VISIBLE;
    }

    public boolean onBackPressed() {
        if (TAG_EMOTICON_SEARCH_FRAGMENT.equals(mCurrentFragmentTag)){
            switchFragment(TAG_EMOTICON_FRAGMENT);
            return true;
        } else if (TAG_GIF_SEARCH_FRAGMENT.equals(mCurrentFragmentTag)) {
            switchFragment(TAG_GIF_FRAGMENT);
            return true;
        }
        return false;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TAG_EMOTICON_FRAGMENT, TAG_EMOTICON_SEARCH_FRAGMENT, TAG_GIF_FRAGMENT, TAG_GIF_SEARCH_FRAGMENT})
    @interface FragmentBackStackTags {
    }

    public final static class EmoticonConfig {

        @ColorInt
        private int headerFooterColor;
        @ColorInt
        private int bodyColor;
        @ColorInt
        private int iconPressInColor;
        @ColorInt
        private int iconPressOutColor;
        @ColorInt
        private int tabDividerColor;

        @Nullable
        private EmoticonProvider mEmoticonProvider;

        @Nullable
        private EmoticonSelectListener mEmoticonSelectListener;

        public EmoticonConfig() {
            //Do nothing
        }

        public EmoticonConfig(@Nullable EmoticonProvider emoticonProvider,
                              @Nullable EmoticonSelectListener emoticonSelectListener) {
            mEmoticonProvider = emoticonProvider;
            mEmoticonSelectListener = emoticonSelectListener;
        }

        @NonNull
        public EmoticonConfig setEmoticonSelectListener(@Nullable EmoticonSelectListener emoticonSelectListener) {
            mEmoticonSelectListener = emoticonSelectListener;
            return this;
        }

        @NonNull
        public EmoticonConfig setEmoticonProvider(@Nullable EmoticonProvider emoticonProvider) {
            mEmoticonProvider = emoticonProvider;
            return this;
        }

        public int getHeaderFooterColor() {
            return headerFooterColor;
        }

        public EmoticonConfig setHeaderFooterColor(@ColorInt int headerFooterColor) {
            this.headerFooterColor = headerFooterColor;
            return this;
        }

        public int getBodyColor() {
            return bodyColor;
        }

        public EmoticonConfig setBodyColor(@ColorInt int bodyColor) {
            this.bodyColor = bodyColor;
            return this;
        }

        public int getIconPressInColor() {
            return iconPressInColor;
        }

        public EmoticonConfig setIconPressInColor(@ColorInt int iconPressInColor) {
            this.iconPressInColor = iconPressInColor;
            return this;
        }

        public int getIconPressOutColor() {
            return iconPressOutColor;
        }

        public EmoticonConfig setIconPressOutColor(@ColorInt int iconPressOutColor) {
            this.iconPressOutColor = iconPressOutColor;
            return this;
        }

        public int getTabDividerColor() {
            return tabDividerColor;
        }

        public EmoticonConfig setTabDividerColor(@ColorInt int tabDividerColor) {
            this.tabDividerColor = tabDividerColor;
            return this;
        }
    }

    public final static class GIFConfig {

        @NonNull
        private final GifProviderProtocol mGifProviderProtocol;
        @ColorInt
        private int headerFooterColor;
        @ColorInt
        private int bodyColor;
        @ColorInt
        private int iconPressInColor;
        @ColorInt
        private int iconPressOutColor;
        @Nullable
        private GifSelectListener mGifSelectListener;

        public GIFConfig(@NonNull GifProviderProtocol gifProviderProtocol) {
            mGifProviderProtocol = gifProviderProtocol;
        }

        @NonNull
        public GIFConfig setGifSelectListener(@Nullable GifSelectListener gifSelectListener) {
            mGifSelectListener = gifSelectListener;
            return this;
        }

        public int getHeaderFooterColor() {
            return headerFooterColor;
        }

        public GIFConfig setHeaderFooterColor(@ColorInt int headerFooterColor) {
            this.headerFooterColor = headerFooterColor;
            return this;
        }

        public int getBodyColor() {
            return bodyColor;
        }

        public GIFConfig setBodyColor(@ColorInt int bodyColor) {
            this.bodyColor = bodyColor;
            return this;
        }

        public int getIconPressInColor() {
            return iconPressInColor;
        }

        public GIFConfig setIconPressInColor(@ColorInt int iconPressInColor) {
            this.iconPressInColor = iconPressInColor;
            return this;
        }

        public int getIconPressOutColor() {
            return iconPressOutColor;
        }

        public GIFConfig setIconPressOutColor(@ColorInt int iconPressOutColor) {
            this.iconPressOutColor = iconPressOutColor;
            return this;
        }
    }

}
