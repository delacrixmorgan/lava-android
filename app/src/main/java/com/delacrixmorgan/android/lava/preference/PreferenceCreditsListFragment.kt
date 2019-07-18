package com.delacrixmorgan.android.lava.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.delacrixmorgan.android.lava.R
import com.delacrixmorgan.android.lava.launchWebsite
import kotlinx.android.synthetic.main.fragment_preference_credits_list.*

class PreferenceCreditsListFragment : DialogFragment() {
    companion object {
        fun newInstance() = PreferenceCreditsListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preference_credits_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.unsplashTextView.setOnClickListener {
            val url = "https://unsplash.com"
            launchWebsite(url)
        }
        
        this.lottieTextView.setOnClickListener {
            val url = "https://lottiefiles.com/lottiefiles"
            launchWebsite(url)
        }

        this.doneButton.setOnClickListener {
            dismiss()
        }
    }
}