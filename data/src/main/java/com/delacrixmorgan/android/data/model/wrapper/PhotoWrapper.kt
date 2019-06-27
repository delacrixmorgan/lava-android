package com.delacrixmorgan.android.data.model.wrapper

import com.delacrixmorgan.android.data.model.User

/**
 *  PhotoWrapper
 *  lava-android
 *
 *  Created by Delacrix Morgan on 25/6/2019.
 *  Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class PhotoWrapper(
    val id: String,
    val urls: Map<String, String>,
    val user: User
)