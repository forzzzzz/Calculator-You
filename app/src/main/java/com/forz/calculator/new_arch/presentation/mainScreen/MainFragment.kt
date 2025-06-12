package com.forz.calculator.new_arch.presentation.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forz.calculator.databinding.FragmentMain2Binding
import dagger.hilt.android.AndroidEntryPoint
import com.forz.calculator.R
import com.forz.calculator.new_arch.presentation.base.TransitionType
import com.forz.calculator.new_arch.presentation.mainScreen.base.BaseMainFragment


@AndroidEntryPoint
class MainFragment() : BaseMainFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_main2
    override fun drawerLayout(): Int = R.id.drawer_layout_main_fragment_dl
    override fun navigationView(): Int = R.id.navigation_view_main_fragment_nv
    override fun getMainNavGraphId(): Int = R.navigation.main_navigation
    override fun getCalculatorNavGraphId(): Int = R.navigation.calculator_navigation
    override fun getMainFragmentId(): Int = R.id.mainFragment
    override fun getCalculatorFragmentId(): Int = R.id.calculatorFragment
    override fun getUnitConverterFragmentId(): Int = R.id.unitConverterFragment
    override fun getCurrencyConverterFragmentId(): Int = R.id.currencyConverterFragment
    override fun getSettingsFragmentId(): Int = R.id.settingsFragment
    override fun getAboutAppFragmentId(): Int = R.id.aboutAppFragment

    private lateinit var binding: FragmentMain2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMain2Binding.inflate(inflater, container, false)

        return binding.root
    }
}