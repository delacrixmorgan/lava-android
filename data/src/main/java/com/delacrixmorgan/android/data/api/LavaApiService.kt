package com.delacrixmorgan.android.data.api

import android.content.Context
import com.delacrixmorgan.android.data.R
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.data.model.PhotoWrapper
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * LavaApiService
 * lava-android
 *
 * Created by Delacrix Morgan on 20/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface LavaApiService {
    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"

        fun create(context: Context): LavaApiService {
            val accessKey = context.getString(R.string.unsplash_access_key)
            val builder = OkHttpClient.Builder()

            builder.addInterceptor {
                val headers = it.request().headers().newBuilder().add("Authorization", "Client-ID $accessKey").build()
                val request = it.request().newBuilder().headers(headers).build()

                it.proceed(request)
            }

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(LavaApiService::class.java)
        }
    }

    @GET("photos/random")
    fun loadRandomPhotos(@Query("count") count: Int = 3, @Query("page") page: Int = 1): Call<Array<PhotoWrapper>>
}