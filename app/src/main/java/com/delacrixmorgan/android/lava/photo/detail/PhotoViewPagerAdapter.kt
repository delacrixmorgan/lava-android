package com.delacrixmorgan.android.lava.photo.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.delacrixmorgan.android.data.model.Photo

/**
 * PhotoViewPagerAdapter
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class PhotoViewPagerAdapter(fragment: Fragment, private val photos: ArrayList<Photo>) : FragmentStatePagerAdapter(fragment.childFragmentManager) {
    override fun getCount() = this.photos.size
    override fun getItem(position: Int): Fragment {
        val photo = this.photos[position]
        return PhotoDetailFragment.newInstance(photo)
    }
}