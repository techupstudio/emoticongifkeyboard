package com.techupstudio.emoticongifkeyboard.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.techupstudio.emoticongifkeyboard.core.interfaces.EmoticonsCategories;
import com.techupstudio.emoticongifkeyboard.core.model.Emoticon;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This is singleton class to handle the recent emoticons and recently selected categories.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public final class RecentEmoticonManager {

    /**
     * Name of the preference file to store all the emoticons data.
     */
    private static final String PREFERENCE_NAME = "emojicon";

    /**
     * Preference key of the list of all the recently used emoticons.
     */
    private static final String KEY_PREF_RECENT = "recent_emojis";

    /**
     * Preference key to hold the {@link EmoticonsCategories}, opened last.
     */
    private static final String KEY_PREF_PAGE = "recent_page";

    /**
     * Sole instance.
     */
    @SuppressLint("StaticFieldLeak")
    private static RecentEmoticonManager sInstance;

    /**
     * List of all the recently used emoticons.
     */
    @NonNull
    private final ArrayList<Emoticon> mRecentEmoticon;

    private OnRecentEmoticonListChangedListener listChangedListener;

    /**
     * Context of the caller.
     */
    private Context mContext;

    /**
     * Private constructor. Not to be called by other class. Use {@link RecentEmoticonManager#getInstance(Context)}
     * instead.
     *
     * @param context Instance.
     */
    private RecentEmoticonManager(@NonNull Context context) {
        mContext = context.getApplicationContext();

        //Load all the recently used emoticons in the memory.
        mRecentEmoticon = loadRecentEmoticons();
    }

    /**
     * Get the singleton instance of this class.
     *
     * @param context Instance of the caller.
     * @return Sole instance.
     */
    public static RecentEmoticonManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecentEmoticonManager.class) {
                if (sInstance == null) {
                    sInstance = new RecentEmoticonManager(context);
                }
            }
        }
        return sInstance;
    }

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @return Last opened {@link EmoticonsCategories}.
     */
    @SuppressWarnings("WrongConstant")
    @EmoticonsCategories.EmoticonsCategory
    public int getLastCategory() {
        return getPreferences().getInt(KEY_PREF_PAGE, EmoticonsCategories.RECENT);
    }

    /**
     * Set the recently opened category.
     *
     * @param recentCategory recently opened category from {@link EmoticonsCategories}.
     */
    public void setLastCategory(@EmoticonsCategories.EmoticonsCategory int recentCategory) {
        getPreferences().edit().putInt(KEY_PREF_PAGE, recentCategory).apply();
    }

    /**
     * Load all the recently used emoticons. This will parse the shared preference of the recent
     * emoticons and return them into an array list.
     */
    @NonNull
    private ArrayList<Emoticon> loadRecentEmoticons() {
        ArrayList<Emoticon> emoticons = new ArrayList<>();
        //Read string from preferences.
        String str = getPreferences().getString(KEY_PREF_RECENT, "");
        StringTokenizer tokenizer = new StringTokenizer(str, "~");
        while (tokenizer.hasMoreTokens()) {
            try {
                emoticons.add(new Emoticon(tokenizer.nextToken()));
            } catch (NumberFormatException e) {
                // ignored
            }
        }
        return emoticons;
    }

    /**
     * Add new emoticons to the recent list. This will always add new emoticon to the first position
     * to keep most recent emoticons first. Once the list gets updated {@link #saveRecentEmoticons()}
     * method will be called to save updated list.
     *
     * @param emoticon {@link Emoticon} to add.
     * @see #saveRecentEmoticons()
     */
    public void add(@NonNull Emoticon emoticon) {
        //Check if the emoticon is  already present?
        //We don't want duplicates

        int index = -1;

        for (int i = 0; i < mRecentEmoticon.size(); i++)
            if (emoticon.equals(mRecentEmoticon.get(i))) {
                index = i;
                break;
            }

        //Add new one to the list
        if (index != -1) mRecentEmoticon.remove(index);
        mRecentEmoticon.add(0, emoticon);
        //Save the updated list to preference
        saveRecentEmoticons();
    }

    /**
     * @return List of recently used {@link Emoticon}.
     */
    @NonNull
    public ArrayList<Emoticon> getRecentEmoticons() {
        return mRecentEmoticon;
    }

    /**
     * Save the recent emoticons list. This will save first 100 emoticons unicode to the shared
     * preference with '~' saturation character.
     */
    private void saveRecentEmoticons() {
        StringBuilder str = new StringBuilder();
        int c = Math.min(mRecentEmoticon.size(), 100);

        //Create the string.
        for (int i = 0; i < c; i++) {
            Emoticon e = mRecentEmoticon.get(i);
            str.append(e.getUnicode());
            if (i < (c - 1)) str.append('~');
        }

        //Save to the preference.
        getPreferences()
                .edit()
                .putString(KEY_PREF_RECENT, str.toString())
                .apply();

        if (listChangedListener != null) listChangedListener.onChanged(mRecentEmoticon);
    }

    /**
     * Reset all the recent emoticons and last selected category.
     */
    public void reset() {
        getPreferences().edit().clear().apply();
    }

    public OnRecentEmoticonListChangedListener getRecentEmoticonListChangedListener() {
        return listChangedListener;
    }

    public void setRecentEmoticonListChangedListener(OnRecentEmoticonListChangedListener listChangedListener) {
        this.listChangedListener = listChangedListener;
    }

    public interface OnRecentEmoticonListChangedListener {
        void onChanged(ArrayList<Emoticon> emoticonArrayList);
    }
}
