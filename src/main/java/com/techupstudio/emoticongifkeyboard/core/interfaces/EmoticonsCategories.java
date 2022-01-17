package com.techupstudio.emoticongifkeyboard.core.interfaces;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Keval on 18-Aug-17.
 * This class has the list of all the emoticons categories.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public final class EmoticonsCategories {
    /**
     * Category of all the recently used emoticons.
     */
    public static final int RECENT = 0;

    /**
     * Category for all face, hands and pearson emoticons.
     */
    public static final int PEOPLE = 1;

    /**
     * Category for nature and animal emoticons.
     */
    public static final int NATURE = 2;

    /**
     * Category for food emoticons.
     */
    public static final int FOOD = 3;

    /**
     * Category for human activities and sport emoticons.
     */
    public static final int ACTIVITY = 4;

    /**
     * Category for travel, places and vehicle emoticons.
     */
    public static final int TRAVEL = 5;

    /**
     * Category for different object emoticons.
     */
    public static final int OBJECTS = 6;

    /**
     * Category for symbols, arrow and other symbol.
     */
    public static final int SYMBOLS = 7;

    /**
     * Category for flags.
     */
    public static final int FLAGS = 8;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RECENT, PEOPLE, NATURE, FOOD, ACTIVITY, TRAVEL, OBJECTS, SYMBOLS, FLAGS})
    public @interface EmoticonsCategory {
    }
}
