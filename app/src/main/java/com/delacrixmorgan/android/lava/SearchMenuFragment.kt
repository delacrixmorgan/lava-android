package com.delacrixmorgan.android.lava

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
import com.delacrixmorgan.android.lava.photo.grid.GridPhotoListFragment
import kotlinx.android.synthetic.main.fragment_search_menu.*

/**
 * SearchMenuFragment
 * lava-android
 *
 * Created by Delacrix Morgan on 04/07/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class SearchMenuFragment : Fragment() {

    companion object {
        private const val BACKGROUND_SRC = "https://images.unsplash.com/photo-1548588627-f978862b85e1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80"
        private const val BACKGROUND_URL = "https://unsplash.com/@tobsef"
    }

    private val viewModel: PhotoViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateActionButtonIcon()

        Glide.with(view.context)
                .load(BACKGROUND_SRC)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform())
                .into(this.backgroundImageView)

        this.backgroundImageView.setSaturation(0.2F)

        this.searchView.setQuery(this.viewModel.queryText, true)

        this.authorTextView.setOnClickListener {
            launchWebsite(BACKGROUND_URL)
        }

        this.searchViewCardView.setOnClickListener {
            this.searchView.isFocusable = true
            this.searchView.isIconified = false
            this.searchView.requestFocusFromTouch()
        }

        this.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus && this.isVisible) {
                hideSoftInputKeyboard()
            }
        }

        this.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.queryText = query
                hideSoftInputKeyboard()
                launchGridPhotoListFragment()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.queryText = newText
                updateActionButtonIcon()
                return true
            }
        })

        this.actionButton.setOnClickListener {
            hideSoftInputKeyboard()
            launchGridPhotoListFragment()
        }
    }

    private fun updateActionButtonIcon() {
        if (this.viewModel.queryText.isNullOrBlank()) {
            this.actionButton.setImageResource(R.drawable.ic_arrow_down)
        } else {
            this.actionButton.setImageResource(R.drawable.ic_search)
        }
    }

    private fun launchGridPhotoListFragment() {
        val fragment = GridPhotoListFragment.newInstance()
        this.viewModel.collage.clear()
        this.activity?.supportFragmentManager?.transaction {
            replace(R.id.mainContainer, fragment, fragment::class.java.simpleName)
            addToBackStack(fragment::class.java.simpleName)
        }
    }
}