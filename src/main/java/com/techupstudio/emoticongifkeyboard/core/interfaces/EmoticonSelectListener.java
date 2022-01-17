package com.techupstudio.emoticongifkeyboard.core.interfaces;

import com.techupstudio.emoticongifkeyboard.core.model.Emoticon;

/**
 * Created by Keval on 18-Aug-17.
 * Listener to get notify when any emoticon gets selected from emoticon list or backspace pressed.
 */

public interface EmoticonSelectListener {

    /**
     * Callback to notify when any {@link Emoticon} gets select.
     *
     * @param emoticon {@link Emoticon} selected.
     */
    void emoticonSelected(Emoticon emoticon);

    /**
     * Callback to notify when backspace button is pressed.
     */
    void onBackSpace();
}
