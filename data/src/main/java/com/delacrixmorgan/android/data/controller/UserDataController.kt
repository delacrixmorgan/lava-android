package com.delacrixmorgan.android.data.controller

import com.delacrixmorgan.android.data.model.User

/**
 *  UserDataController.kt
 *  lava-android
 *
 *  Created by Delacrix Morgan on 20/06/2019.
 *  Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object UserDataController {
    private var users = listOf<User>()

    private fun insert(incomingItems: List<User>) {
        val incomingInts = HashSet<String>()
        incomingItems.forEach { incomingInts.add(it.id) }

        val existingMinusIncomingInts = this.users.filter { !incomingInts.contains(it.id) }
        val uniqueIncomingItems = arrayListOf<User>()

        incomingInts.forEach { id ->
            incomingItems.find { it.id == id }?.let {
                uniqueIncomingItems.add(it)
            }
        }
        this.users = ArrayList(existingMinusIncomingInts + uniqueIncomingItems)
    }
}