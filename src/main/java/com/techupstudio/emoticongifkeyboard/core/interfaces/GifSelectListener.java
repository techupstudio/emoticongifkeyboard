package com.techupstudio.emoticongifkeyboard.core.interfaces;

import androidx.annotation.NonNull;

import com.techupstudio.emoticongifkeyboard.core.model.Gif;


public interface GifSelectListener {

    /**
     * @param gif Gif selected.
     */
    void onGifSelected(@NonNull Gif gif);
}
