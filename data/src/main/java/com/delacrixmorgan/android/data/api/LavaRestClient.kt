package com.delacrixmorgan.android.data.api

/**
 * LavaRestClient
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class LavaRestClient {
    interface LoadListListener<T> {
        fun onComplete(list: List<T> = listOf(), error: Exception? = null)
    }
}