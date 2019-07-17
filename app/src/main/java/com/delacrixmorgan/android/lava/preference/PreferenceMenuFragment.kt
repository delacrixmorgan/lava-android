package com.delacrixmorgan.android.lava.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delacrixmorgan.android.lava.BuildConfig
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.databinding.FragmentPreferenceMenuBinding
import com.delacrixmorgan.android.lava.launchWebsite
import com.delacrixmorgan.android.lava.shareAppIntent
import kotlinx.android.synthetic.main.fragment_preference_menu.*

class PreferenceMenuFragment : Fragment() {

    companion object {
        const val SOURCE_CODE_URL = "https://github.com/delacrixmorgan/lava-android"

        fun newInstance() = PreferenceMenuFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentPreferenceMenuBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.buildNumberTextView.text = getString(R.string.message_build_version_name, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        this.creditsViewGroup.setOnClickListener {

        }

        this.sourceCodeViewGroup.setOnClickListener {
            launchWebsite(SOURCE_CODE_URL)
        }

        this.shareViewGroup.setOnClickListener {
            view.context.shareAppIntent(getString(R.string.fragment_preference_menu_body_share_message))
        }
    }
}