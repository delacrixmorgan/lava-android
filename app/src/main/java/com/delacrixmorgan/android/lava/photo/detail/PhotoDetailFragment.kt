package com.delacrixmorgan.android.lava.photo.detail

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
        postponeEnterTransition()

        if (this.sharedElementEnterTransition == null) {
            this.sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.image_transition)?.apply {
                duration = 375
            }
        }

        this.arguments?.getString(ARG_PHOTO_ID)?.let { id ->
            this.photo = PhotoDataController.getPhotoById(id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

        ViewCompat.setTransitionName(this.gridImageView, this.photo?.getUrl(Photo.UrlType.THUMB))
        Glide.with(view.context)
                .load(this.photo?.getUrl(Photo.UrlType.THUMB))
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        bigImageView.showImage(Uri.parse(photo?.getUrl(Photo.UrlType.THUMB)), Uri.parse(photo?.getUrl(Photo.UrlType.REGULAR)))
                        bigImageView.isVisible = true

                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                })
                .apply(requestOptions)
                .into(this.gridImageView)
    }
}