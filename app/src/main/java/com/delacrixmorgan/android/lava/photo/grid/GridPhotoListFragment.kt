package com.delacrixmorgan.android.lava.photo.grid

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.delacrixmorgan.android.data.api.LavaRestClient
import com.delacrixmorgan.android.data.controller.PhotoDataController
import com.delacrixmorgan.android.data.model.CuratedType
import com.delacrixmorgan.android.data.model.Photo
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.common.AspectRatioGridLayoutManager
import com.delacrixmorgan.android.lava.compatColor
import com.delacrixmorgan.android.lava.performHapticContextClick
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
import com.delacrixmorgan.android.lava.photo.detail.PhotoListFragment
import com.github.piasy.biv.BigImageViewer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cell_photo.view.*
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

    companion object {
        private const val GRID_LAYOUT_ASPECT_RATIO = 1.6F
        private const val SPAN_COUNT = 3

        fun newInstance(): GridPhotoListFragment {
            return GridPhotoListFragment()
        }
    }

    private val enterTransitionStarted = AtomicBoolean()

    private lateinit var viewModel: PhotoViewModel
    private lateinit var adapter: GridPhotoRecyclerViewAdapter
    private lateinit var layoutManager: AspectRatioGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = ViewModelProviders.of(requireActivity()).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grid_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareTransitions()

        if (this.viewModel.displayMetrics == null) {
            this.viewModel.displayMetrics = this.resources.displayMetrics
        }

        val maxHeight = this.viewModel.heightPixels / SPAN_COUNT

        this.layoutManager = AspectRatioGridLayoutManager(
            context = view.context,
            spanCount = SPAN_COUNT,
            aspectRatio = GRID_LAYOUT_ASPECT_RATIO,
            fittingSize = this.viewModel.widthPixels
        )
        this.adapter = GridPhotoRecyclerViewAdapter(maxHeight, this)
        this.adapter.updateDataSet(this.viewModel.collage)


        this.recyclerView.addOnLayoutChangeListener(this)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = this.layoutManager

        this.loadingAnimationView.isVisible = this.viewModel.collage.isEmpty()

        this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount - visibleItemCount
                        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                        if (pastVisibleItems + visibleItemCount >= totalItemCount && adapter.itemCount > 0) {
                            refreshFromServer()
                        }
                    }
                }
            }
        })

        this.swipeRefreshLayout.setColorSchemeColors(
            R.color.colorPrimary.compatColor(this.context),
            R.color.colorPrimaryDark.compatColor(this.context),
            R.color.colorAccent.compatColor(this.context),
            R.color.colorPrimaryLight.compatColor(this.context)
        )

        this.swipeRefreshLayout.setOnRefreshListener {
            this.viewModel.collage.clear()
            this.adapter.removeDataSet()

            this.swipeRefreshLayout.performHapticContextClick()
            refreshFromServer()
        }

        if (this.adapter.itemCount == 0) {
            refreshFromServer()
        }

        if (savedInstanceState != null) {
            restorePhotoListFragment()
        }
    }

    private fun restorePhotoListFragment() {
        this.activity?.supportFragmentManager?.apply {
            findFragmentByTag(PhotoListFragment::class.java.simpleName)?.let { fragment ->
                transaction {
                    replace(R.id.mainContainer, fragment)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun prepareTransitions() {
        this.exitTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.grid_exit_transition).apply {
                duration = 375
                startDelay = 25
            }

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

    private fun searchFromServer(query: String?) {
        if (query == null) {
            refreshFromServer()
            return
        }

        val page = (this.adapter.itemCount / 30) + 1
        PhotoDataController.searchPhotos(
            requireContext(),
            query = query,
            page = page,
            listener = object : LavaRestClient.LoadListListener<Photo> {
                override fun onComplete(list: List<Photo>, error: Exception?) {
                    error?.let {
                        Snackbar.make(rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                        return
                    }

                    BigImageViewer.prefetch(*list.mapNotNull { Uri.parse(it.getUrl(Photo.UrlType.REGULAR)) }.toTypedArray())
                    val previousPosition = viewModel.collage.size

                    viewModel.collage.addAll(list)
                    adapter.updateDataSet(viewModel.collage)

                    if (isVisible) recyclerView.smoothScrollToPosition(previousPosition)
                }
            })
    }

    private fun refreshFromServer() {
        if (this.viewModel.queryText?.isNotBlank() == true) {
            searchFromServer(this.viewModel.queryText)
            return
        }

        val page = (this.adapter.itemCount / 30) + 1
        PhotoDataController.loadCuratedPhotos(
            requireContext(),
            page = page,
            curatedType = CuratedType.LATEST,
            listener = object : LavaRestClient.LoadListListener<Photo> {
                override fun onComplete(list: List<Photo>, error: Exception?) {
                    loadingAnimationView.isVisible = false

                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.isRefreshing = false
                    }

                    error?.let {
                        Snackbar.make(rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                        return
                    }

                    BigImageViewer.prefetch(*list.mapNotNull { Uri.parse(it.getUrl(Photo.UrlType.REGULAR)) }.toTypedArray())

                    val previousPosition = viewModel.collage.size

                    viewModel.collage.addAll(list)
                    adapter.updateDataSet(viewModel.collage)

                    if (isVisible) recyclerView.smoothScrollToPosition(previousPosition)
                }
            })
    }

    //region GridPhotoListListener
    override fun onPhotoSelected(viewHolder: View, photo: Photo, position: Int) {
        this.viewModel.currentPosition = position
        this.viewModel.isDetailShowing = false

        (this.exitTransition as TransitionSet).excludeTarget(viewHolder, true)

        val imageListFragment = PhotoListFragment()
        val transitionName = ViewCompat.getTransitionName(viewHolder.gridImageView)
            ?: "gridImageView"

        this.activity?.supportFragmentManager?.transaction {
            addSharedElement(viewHolder.gridImageView, transitionName)
            replace(R.id.mainContainer, imageListFragment)
            addToBackStack(null)
        }
    }

    override fun onLoadCompleted(view: View, position: Int) {
        if (this.viewModel.currentPosition != position || this.enterTransitionStarted.getAndSet(true)) return
        startPostponedEnterTransition()
    }
    //endregion

    //region View.OnLayoutChangeListener
    override fun onLayoutChange(
        view: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        this.recyclerView.removeOnLayoutChangeListener(this)
        val layoutManager = this.recyclerView.layoutManager ?: return
        val currentPosition = this.viewModel.currentPosition
        val viewAtPosition = layoutManager.findViewByPosition(currentPosition)

        if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)) {
            this.recyclerView.post { layoutManager.scrollToPosition(currentPosition) }
        }
    }
    //endregion
}