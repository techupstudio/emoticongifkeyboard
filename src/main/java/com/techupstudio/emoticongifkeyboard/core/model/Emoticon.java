package com.techupstudio.emoticongifkeyboard.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;


public final class Emoticon implements Parcelable {

    public static final Creator<Emoticon> CREATOR = new Creator<Emoticon>() {
        @Override
        public Emoticon createFromParcel(Parcel in) {
            return new Emoticon(in);
        }

        @Override
        public Emoticon[] newArray(int size) {
            return new Emoticon[size];
        }
    };
    /**
     * Unicode value of the emoticon.
     */
    @NonNull
    private final String unicode;
    /**
     * Custom icon for the emoticon. (If you don't want to use system default ones.)
     */
    @DrawableRes
    private int icon = -1;

    /**
     * Public constructor.
     *
     * @param unicode Unicode of the emoticon. This cannot be null.
     */
    public Emoticon(@NonNull String unicode) {
        //noinspection ConstantConditions
        if (unicode == null) throw new RuntimeException("Unicode cannot be null.");
        this.unicode = unicode;
    }

    /**
     * Public constructor.
     *
     * @param unicode Unicode of the emoticon. This cannot be null.
     * @param icon    Drawable resource id for the emoticon.
     */

    public Emoticon(@NonNull String unicode, @DrawableRes int icon) {
        this(unicode);
        this.icon = icon;
    }

    /**
     * Constructor for parcelable object.
     */
    public Emoticon(Parcel in) {
        this.icon = in.readInt();
        this.unicode = in.readString();

        //noinspection ConstantConditions
        if (unicode == null) throw new RuntimeException("Unicode cannot be null.");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(unicode);
    }

    /**
     * @return Drawable resource for the image of emoticon. If there is no icon, it will return -1.
     */
    @DrawableRes
    public int getIcon() {
        return icon;
    }

    /**
     * @return Unicode for the emoticon.
     */
    @NonNull
    public String getUnicode() {
        return unicode;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Emoticon && unicode.equals(((Emoticon) o).unicode));
    }

    @Override
    public int hashCode() {
        return unicode.hashCode();
    }
}
