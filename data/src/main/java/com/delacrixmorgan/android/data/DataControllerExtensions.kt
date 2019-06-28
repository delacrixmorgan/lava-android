package com.delacrixmorgan.android.data

import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.controller.UserDataController
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.data.model.User
import com.delacrixmorgan.android.data.model.wrapper.PhotoWrapper

/**
 * DataControllerExtensions
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

fun List<PhotoWrapper>.processPhotos(): List<Photo> {
    val photos = this.map { it.extractPhoto() }
    val users = this.map { it.extractUser() }

    PhotoDataController.processResponse(photos)
    UserDataController.processResponse(users)

    return photos
}

fun PhotoWrapper.extractPhoto(): Photo {
    return Photo(id = this.id, urls = this.urls, userId = this.user.id)
}

fun PhotoWrapper.extractUser(): User {
    return User(id = this.user.id, name = this.user.name, links = this.user.links, profileImage = this.user.profileImage)
}