package com.delacrixmorgan.android.lava.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.lava.R
import kotlinx.android.synthetic.main.cell_photo.view.*

/**
 * PhotoRecyclerViewAdapter
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class PhotoRecyclerViewAdapter(
    private val maxHeight: Int,
    private val listener: PhotoListListener
) : RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoItemViewHolder>() {
    private val photos = arrayListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_photo, parent, false)
        itemView.layoutParams.height = this.maxHeight

        return PhotoItemViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        val photo = this.photos[position]
        holder.updateData(photo)
    }

    override fun getItemCount() = this.photos.size

    fun updateDataSet(photos: List<Photo>) {
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }

    inner class PhotoItemViewHolder(itemView: View, listener: PhotoListListener) : RecyclerView.ViewHolder(itemView) {
        private lateinit var photo: Photo

        init {
            this.itemView.setOnClickListener {
                listener.onPhotoSelected(itemView, this.photo, this.adapterPosition)
            }
        }

        fun updateData(photo: Photo) {
            this.photo = photo
            val photoUrl = this.photo.getUrl(Photo.UrlType.THUMB)

            ViewCompat.setTransitionName(this.itemView.gridImageView, photoUrl)

            Glide.with(this.itemView.context)
                .load(photoUrl)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                )
                .into(this.itemView.gridImageView)
        }
    }
}