package com.forz.calculator.new_arch.presentation.about_app

import android.os.Bundle
import android.view.View
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentAboutAppBinding
import com.forz.calculator.new_arch.presentation.base.BaseCollapsingToolbarFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppFragment : BaseCollapsingToolbarFragment() {

    private lateinit var binding: FragmentAboutAppBinding

    override fun getContentLayoutResId() = R.layout.fragment_about_app

    override fun getToolbarTitleResId() = R.string.title_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAboutAppBinding.bind(childFragmentContentView!!)
    }
}