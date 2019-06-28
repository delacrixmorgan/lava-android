package com.delacrixmorgan.android.lava

import android.content.Context
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

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

fun Fragment.hideSoftInputKeyboard() {
    this.view?.hideSoftInputKeyboard()
}


//region View
fun View.hideSoftInputKeyboard() {
    val inputManager = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.performHapticContextClick() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}
//endregion