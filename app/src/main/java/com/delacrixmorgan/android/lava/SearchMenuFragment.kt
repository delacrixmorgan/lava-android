package com.delacrixmorgan.android.lava

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Slide
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
import com.delacrixmorgan.android.lava.photo.grid.GridPhotoListFragment
import com.delacrixmorgan.android.lava.preference.PreferenceMenuFragment
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

        setupSearchView()
        updateActionButtonIcon()

        this.authorTextView.setOnClickListener {
            launchWebsite(BACKGROUND_URL)
        }

        this.menuButton.setOnClickListener {
            launchPreferenceMenuFragment()
        }

        this.actionButton.setOnClickListener {
            hideSoftInputKeyboard()
            launchGridPhotoListFragment()
        }

        this.shareButton.setOnClickListener {
            launchPlayStore(BuildConfig.APPLICATION_ID)
        }
    }

    private fun setupSearchView() {
        this.searchView.setQuery(this.viewModel.queryText, true)

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
        fragment.enterTransition = Slide(Gravity.BOTTOM).setDuration(200)

        this.viewModel.collage.clear()
        this.activity?.supportFragmentManager?.transaction {
            replace(R.id.mainContainer, fragment, fragment::class.java.simpleName)
            addToBackStack(fragment::class.java.simpleName)
        }
    }

    private fun launchPreferenceMenuFragment() {
        val fragment = PreferenceMenuFragment.newInstance()
        fragment.enterTransition = Slide(Gravity.START).setDuration(200)

        this.activity?.supportFragmentManager?.transaction {
            replace(R.id.mainContainer, fragment, fragment::class.java.simpleName)
            addToBackStack(fragment::class.java.simpleName)
        }
    }

    private fun launchPlayStore(packageName: String) {
        val url = "https://play.google.com/store/apps/details?id=$packageName"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}