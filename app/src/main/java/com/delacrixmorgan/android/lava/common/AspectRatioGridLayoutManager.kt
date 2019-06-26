package com.delacrixmorgan.android.lava.common

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * AspectRatioGridLayoutManager
 * lava-android
 *
 * Created by Delacrix Morgan on 26/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class AspectRatioGridLayoutManager(
        context: Context,
        spanCount: Int,
        orientation: Int = RecyclerView.VERTICAL,
        private val aspectRatio: Float,
        private val fittingSize: Int
) : GridLayoutManager(context, spanCount, orientation, false) {
    override fun addView(child: View?) {
        if (this.orientation == VERTICAL) {
            child?.layoutParams?.height = (this.aspectRatio * fittingSize).toInt() / this.spanCount
        } else {
            child?.layoutParams?.width = (this.aspectRatio * fittingSize).toInt() / this.spanCount
        }
        super.addView(child)
    }
}
