package com.delacrixmorgan.android.lava.photo

import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import com.delacrixmorgan.android.data.model.Photo

/**
 * PhotoViewModel
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class PhotoViewModel : ViewModel() {
    var currentPosition = 0
    var collage = arrayListOf<Photo>()

    var queryText: String? = null
    var displayMetrics: DisplayMetrics? = null

    val widthPixels: Int
        get() {
            return this.displayMetrics?.widthPixels ?: 0
        }

    val heightPixels: Int
        get() {
            return this.displayMetrics?.heightPixels ?: 0
        }
}