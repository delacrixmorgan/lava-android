package com.delacrixmorgan.android.data

import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.controller.UserDataController
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.data.model.wrapper.PhotoWrapper
import com.delacrixmorgan.android.data.model.User

/**
 * DataControllerExtensions
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

fun List<PhotoWrapper>.processPhotos(): List<Photo> {
    val photos = this.map { Photo(id = it.id, urls = it.urls, userId = it.user.id) }
    val users = this.map { User(id = it.user.id, name = it.user.name, links = it.user.links, profileImage = it.user.profileImage) }

    PhotoDataController.processResponse(photos)
    UserDataController.processResponse(users)

    return photos
}