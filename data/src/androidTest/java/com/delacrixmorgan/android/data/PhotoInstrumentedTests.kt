package com.delacrixmorgan.android.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delacrixmorgan.android.data.api.LavaApiService
import com.delacrixmorgan.android.data.api.LavaRestClient
import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.data.model.PhotoWrapper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

/**
 * PhotoInstrumentedTests
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@RunWith(AndroidJUnit4::class)
class PhotoInstrumentedTests {
    private val appContext = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun loadRandomPhotos() {
        val signal = CountDownLatch(1)

        LavaApiService.create(this.appContext)
                .loadRandomPhotos(3)
                .enqueue(object : Callback<Array<PhotoWrapper>> {
                    override fun onResponse(call: Call<Array<PhotoWrapper>>, response: Response<Array<PhotoWrapper>>) {
                        Assert.assertTrue("Response Body is Empty", response.body() != null)
                        signal.countDown()
                    }

                    override fun onFailure(call: Call<Array<PhotoWrapper>>, t: Throwable) {
                        Assert.fail(t.message)
                        signal.countDown()
                    }
                })
        signal.await()
    }

    @Test
    fun loadRandomPhotosWithDataController() {
        val signal = CountDownLatch(1)

        PhotoDataController.loadRandomPhotos(this.appContext, listener = object : LavaRestClient.LoadListListener<Photo> {
            override fun onComplete(list: List<Photo>, error: Exception?) {
                error?.let {
                    Assert.fail(it.message)
                    signal.countDown()
                    return
                }

                Assert.assertTrue("Photos are Empty", list.isNotEmpty())
                signal.countDown()
            }
        })

        signal.await()
    }
}