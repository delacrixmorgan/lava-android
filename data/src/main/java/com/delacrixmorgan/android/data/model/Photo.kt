package com.delacrixmorgan.android.data.model

import com.delacrixmorgan.android.data.controller.UserDataController

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
    val links: Map<String, String>,
    val userId: String
) {
    val user: User?
        get() {
            return UserDataController.getUserById(this.userId)
        }

    fun getUrl(urlType: UrlType): String {
        return urls[urlType.name.toLowerCase()].toString()
    }

    fun getLink(linkType: LinkType): String {
        return links[linkType.name.toLowerCase()].toString()
    }

    enum class UrlType {
        RAW,
        FULL,
        REGULAR,
        SMALL,
        THUMB
    }

    enum class LinkType {
        SELF,
        HTML,
        DOWNLOAD,
        DOWNLOAD_LOCATION
    }
}