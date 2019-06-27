package com.delacrixmorgan.android.data.model.wrapper

import com.squareup.moshi.Json

/**
 *  SearchWrapper
 *  lava-android
 *
 *  Created by Delacrix Morgan on 27/6/2019.
 *  Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class SearchWrapper(
        @field:Json(name = "total")
        val totalResult: Int,
        @field:Json(name = "total_pages")
        val totalPages: Int,
        @field:Json(name = "results")
        val photos: List<PhotoWrapper>
)