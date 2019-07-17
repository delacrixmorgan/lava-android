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
import androidx.lifecycle.ViewModelProviders
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
import com.delacrixmorgan.android.data.model.User
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.launchWebsite
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
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
    private val viewModel: PhotoViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PhotoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.userVisibleHint = false
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

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible && isVisible) {
            toggleViewGroupVisibility()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.bigImageView.setOnClickListener {
            this.viewModel.isDetailShowing = !this.viewModel.isDetailShowing
            toggleViewGroupVisibility()
        }

        this.backButton.setOnClickListener {
            this.activity?.onBackPressed()
        }

        val authorName = this.photo?.user?.name
        this.authorTextView.text = "Photo by $authorName on Unsplash"
        this.authorTextView.setOnClickListener {
            this.photo?.user?.getLink(User.LinkType.HTML)?.let { url ->
                launchWebsite(url)
            }
        }

        ViewCompat.setTransitionName(this.gridImageView, this.photo?.getUrl(Photo.UrlType.THUMB))
        Glide.with(view.context)
                .load(this.photo?.getUrl(Photo.UrlType.THUMB))
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (bigImageView != null) {
                            bigImageView.showImage(Uri.parse(photo?.getUrl(Photo.UrlType.THUMB)), Uri.parse(photo?.getUrl(Photo.UrlType.REGULAR)))
                            bigImageView.isVisible = true
                        }

                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                })
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform())
                .into(this.gridImageView)
    }

    private fun toggleViewGroupVisibility() {
        this.topViewGroup.isVisible = this.viewModel.isDetailShowing
        this.bottomViewGroup.isVisible = this.viewModel.isDetailShowing
    }
}