package com.delacrixmorgan.android.lava.photo.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.lava.R
import kotlinx.android.synthetic.main.fragment_photo_detail.*

/**
 * PhotoDetailFragment
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class PhotoDetailFragment : Fragment() {

    companion object {
        private const val ARG_PHOTO_ID = "Photo.id"

        fun newInstance(photo: Photo): PhotoDetailFragment {
            return PhotoDetailFragment().apply {
                this.arguments = bundleOf(ARG_PHOTO_ID to photo.id)
            }
        }
    }

    private var photo: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.arguments?.getString(ARG_PHOTO_ID)?.let { id ->
            this.photo = PhotoDataController.getPhotoById(id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.subSamplingImageView.showImage(Uri.parse(this.photo?.getUrl(Photo.UrlType.THUMB)), Uri.parse(this.photo?.getUrl(Photo.UrlType.REGULAR)))
    }
}