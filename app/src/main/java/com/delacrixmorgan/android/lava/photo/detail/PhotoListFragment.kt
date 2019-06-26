package com.delacrixmorgan.android.lava.photo.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.photo.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_photo_list.*

/**
 * PhotoListFragment
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class PhotoListFragment : Fragment(), ViewPager.OnPageChangeListener {

    private lateinit var adapter: PhotoViewPagerAdapter
    private val viewModel: PhotoViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PhotoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.image_transition).apply {
            duration = 375
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSharedElementTransition()

        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        this.adapter = PhotoViewPagerAdapter(this)
        this.adapter.updateDataSet(this.viewModel.collage)

        this.viewPager.adapter = this.adapter
        this.viewPager.setCurrentItem(this.viewModel.currentPosition, false)
        this.viewPager.addOnPageChangeListener(this)
    }

    private fun prepareSharedElementTransition() {
        this.sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.image_transition).apply {
            duration = 375
        }

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>) {
                val currentFragment = adapter.instantiateItem(viewPager, viewModel.currentPosition) as Fragment
                val view = currentFragment.view ?: return
                val firstName = names?.get(0) ?: return

                sharedElements[firstName] = view.findViewById(R.id.gridImageView)
            }
        })
    }

    //region ViewPager.OnPageChangeListener
    override fun onPageSelected(position: Int) {
        this.viewModel.currentPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
    //endregion
}
