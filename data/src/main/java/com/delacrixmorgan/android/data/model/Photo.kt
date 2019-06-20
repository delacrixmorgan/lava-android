package com.delacrixmorgan.android.data.model

/**
 * Photo
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class Photo(
        val id: String,
        val urls: Map<String, String>,
        val user: User
)