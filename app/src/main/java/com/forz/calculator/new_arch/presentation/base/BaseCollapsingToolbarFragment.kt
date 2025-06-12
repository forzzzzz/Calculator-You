package com.forz.calculator.new_arch.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import com.forz.calculator.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseCollapsingToolbarFragment : BaseFragment(TransitionType.INSIDE) {

    protected lateinit var toolbar: MaterialToolbar
        private set

    protected var childFragmentContentView: View? = null
        private set


    @LayoutRes
    protected abstract fun getContentLayoutResId(): Int

    @StringRes
    protected abstract fun getToolbarTitleResId(): Int

    @MenuRes
    protected open fun getToolbarMenuResId(): Int = 0

    protected open fun onToolbarMenuItemClick(item: MenuItem): Boolean = false

    protected fun onNavigationIconClicked() {
        findNavController().navigateUp()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val baseView = inflater.inflate(R.layout.fragment_base_collapsing_toolbar, container, false)
        toolbar = baseView.findViewById(R.id.top_app_bar_base)
        val contentHostContainer = baseView.findViewById<FrameLayout>(R.id.fragment_content_container)

        val contentLayoutId = getContentLayoutResId()
        if (contentLayoutId != 0) {
            val inflatedContent = inflater.inflate(contentLayoutId, contentHostContainer, false)
            childFragmentContentView = inflatedContent
            contentHostContainer.addView(inflatedContent)
        } else {
            childFragmentContentView = null
        }

        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setTitle(getToolbarTitleResId())

        toolbar.setNavigationOnClickListener {
            onNavigationIconClicked()
        }

        val menuResId = getToolbarMenuResId()
        if (menuResId != 0) {
            toolbar.inflateMenu(menuResId)
            toolbar.setOnMenuItemClickListener { item ->
                onToolbarMenuItemClick(item)
            }
        } else {
            toolbar.menu.clear()
            toolbar.setOnMenuItemClickListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        childFragmentContentView = null
    }
}