package com.techupstudio.emoticongifkeyboard.core.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.techupstudio.emoticongifkeyboard.core.model.Gif;

import java.util.List;


public interface GifProviderProtocol {

    /**
     * Load the trending GIFs list.
     *
     * @param limit Number of GIFs.
     * @return List of all the {@link Gif} or null of the error occurs.
     */
    @Nullable
    @WorkerThread
    List<Gif> getTrendingGifs(int limit);


    /**
     * Search GIFs list.
     *
     * @param limit Number of GIFs.
     * @param query Search query.
     * @return List of all the {@link Gif} or null of the error occurs.
     */
    @Nullable
    @WorkerThread
    List<Gif> searchGifs(int limit, @NonNull String query);

}
