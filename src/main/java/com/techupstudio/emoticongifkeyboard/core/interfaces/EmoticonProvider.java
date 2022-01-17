package com.techupstudio.emoticongifkeyboard.core.interfaces;


import androidx.annotation.DrawableRes;

public interface EmoticonProvider {

    /**
     * Get the drawable resource for the given unicode.
     *
     * @param unicode Unicode for which icon is required.
     * @return Icon drawable resource id or -1 if there is no drawable for given unicode.
     */
    @DrawableRes
    int getIcon(String unicode);

    /**
     * Check if the icon pack contains the icon image for given unicode/emoticon?
     *
     * @param unicode Unicode to check.
     * @return True if the icon found else false.
     */
    boolean hasEmoticonIcon(String unicode);
}