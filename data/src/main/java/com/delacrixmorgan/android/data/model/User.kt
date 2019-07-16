package com.delacrixmorgan.android.data.model

import com.squareup.moshi.Json

/**
 * User
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class User(
        val id: String,
        val name: String,
        val links: Map<String, String>,
        @field:Json(name = "profile_image")
        val profileImage: Map<String, String>
) {
    fun getLink(linkType: LinkType): String {
        return links[linkType.name.toLowerCase()].toString()
    }

    enum class LinkType {
        SELF,
        HTML,
        PHOTOS,
        LIKES,
        PORTFOLIO,
        FOLLOWING,
        FOLLOWERS
    }
}