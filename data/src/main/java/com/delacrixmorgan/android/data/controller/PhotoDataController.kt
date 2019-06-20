package com.delacrixmorgan.android.data.controller

import android.content.Context
import com.delacrixmorgan.android.data.api.LavaApiService
import com.delacrixmorgan.android.data.api.LavaRestClient
import com.delacrixmorgan.android.data.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * PhotoDataController
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object PhotoDataController {
    private var photos = listOf<Photo>()

    private fun loadRandomPhotos(context: Context, itemCount: Int = 3, listener: LavaRestClient.LoadListListener<Photo>) {
        LavaApiService.create(context)
                .loadRandomPhotos(itemCount)
                .enqueue(object : Callback<Array<Photo>> {
                    override fun onResponse(call: Call<Array<Photo>>, response: Response<Array<Photo>>) {
                        val incomingPhotos = response.body()?.toList()
                        if (incomingPhotos != null) {
                            insert(incomingPhotos)
                            listener.onComplete(list = incomingPhotos)
                        } else {
                            listener.onComplete(error = Exception(response.errorBody().toString()))
                        }
                    }

                    override fun onFailure(call: Call<Array<Photo>>, t: Throwable) {
                        listener.onComplete(error = Exception(t.message))
                    }
                })
    }

    private fun insert(incomingItems: List<Photo>) {
        val incomingInts = HashSet<String>()
        incomingItems.forEach { incomingInts.add(it.id) }

        val existingMinusIncomingInts = this.photos.filter { !incomingInts.contains(it.id) }
        val uniqueIncomingItems = arrayListOf<Photo>()

        incomingInts.forEach { id ->
            incomingItems.find { it.id == id }?.let {
                uniqueIncomingItems.add(it)
            }
        }
        this.photos = ArrayList(existingMinusIncomingInts + uniqueIncomingItems)
    }
}