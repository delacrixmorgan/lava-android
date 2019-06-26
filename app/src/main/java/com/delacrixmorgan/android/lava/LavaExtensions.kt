package com.delacrixmorgan.android.lava

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * LavaExtensions
 * lava-android
 *
 * Created by Delacrix Morgan on 26/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

fun Int.compatColor(context: Context?): Int {
    return if (context == null) {
        0
    } else {
        ContextCompat.getColor(context, this)
    }
}