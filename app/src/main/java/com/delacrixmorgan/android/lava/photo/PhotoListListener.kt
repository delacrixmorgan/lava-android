package com.delacrixmorgan.android.lava.photo

import android.view.View
import com.delacrixmorgan.android.data.model.Photo

/**
 * PhotoListListener
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface PhotoListListener {
    fun onPhotoSelected(viewHolder: View, photo: Photo, position: Int)
    fun onLoadCompleted(view: View, position: Int)
}