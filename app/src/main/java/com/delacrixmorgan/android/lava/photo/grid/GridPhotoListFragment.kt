package com.delacrixmorgan.android.lava.photo.grid

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.delacrixmorgan.android.data.api.LavaRestClient
import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_grid_photo_list.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * GridPhotoListFragment
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class GridPhotoListFragment : Fragment(), GridPhotoListListener, View.OnLayoutChangeListener {

    private var spanCount = 3
    private val enterTransitionStarted = AtomicBoolean()
    private val viewModel: PhotoViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PhotoViewModel::class.java)
    }

    private lateinit var adapter: GridPhotoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grid_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxHeight = this.resources.displayMetrics.widthPixels / this.spanCount
        this.adapter = GridPhotoRecyclerViewAdapter(maxHeight, this)

//        this.recyclerView.addOnLayoutChangeListener(this)
        this.recyclerView.adapter = this.adapter

        if (this.adapter.itemCount == 0) {
            refreshDataSet()
        }

//        prepareTransitions()
    }

    private fun prepareTransitions() {
        this.exitTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.grid_exit_transition)

        postponeEnterTransition()
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>) {
                val selectedViewHolder = recyclerView.findViewHolderForAdapterPosition(viewModel.currentPosition)
                val firstName = names?.get(0)

                if (selectedViewHolder != null && firstName != null) {
                    sharedElements[firstName] = selectedViewHolder.itemView.findViewById(R.id.gridImageView)
                }
            }
        })
    }

    private fun refreshDataSet() {
        PhotoDataController.loadRandomPhotos(requireContext(), 48, listener = object : LavaRestClient.LoadListListener<Photo> {
            override fun onComplete(list: List<Photo>, error: Exception?) {
                error?.let {
                    Snackbar.make(rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                    return
                }

                adapter.updateDataSet(list)
            }
        })
    }

    //region GridPhotoListListener
    override fun onPhotoSelected(viewHolder: View, photo: Photo, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadCompleted(view: View, position: Int) {
        if (this.viewModel.currentPosition != position || this.enterTransitionStarted.getAndSet(true)) return
        startPostponedEnterTransition()
    }
    //endregion

    //region View.OnLayoutChangeListener
    override fun onLayoutChange(view: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
    }
    //endregion
}