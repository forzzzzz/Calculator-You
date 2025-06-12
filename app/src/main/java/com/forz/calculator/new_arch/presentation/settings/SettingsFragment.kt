package com.forz.calculator.new_arch.presentation.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentSettingsBinding
import com.forz.calculator.new_arch.presentation.base.BaseCollapsingToolbarFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseCollapsingToolbarFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun getContentLayoutResId() = R.layout.fragment_settings

    override fun getToolbarTitleResId() = R.string.title_settings

    override fun getToolbarMenuResId(): Int = R.menu.settings_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(childFragmentContentView!!)
    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.action_settings -> {
//                Toast.makeText(context, "Settings clicked from About", Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.action_logout -> {
//                Toast.makeText(context, "Logout clicked from About", Toast.LENGTH_SHORT).show()
//                true
//            }
            else -> super.onToolbarMenuItemClick(item) // or false
        }
    }
}