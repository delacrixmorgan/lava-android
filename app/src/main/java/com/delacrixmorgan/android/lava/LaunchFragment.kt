package com.delacrixmorgan.android.lava

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction

/**
 * LaunchFragment
 * lava-android
 *
 * Created by Delacrix Morgan on 25/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchMenuFragment = SearchMenuFragment()
        this.activity?.supportFragmentManager?.transaction {
            replace(R.id.mainContainer, searchMenuFragment, searchMenuFragment::class.java.simpleName)
        }
    }
}