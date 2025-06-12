package com.forz.calculator.new_arch.presentation.mainScreen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.forz.calculator.R
import com.forz.calculator.new_arch.presentation.base.BaseFragment
import com.forz.calculator.new_arch.presentation.base.TransitionType
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

abstract class BaseMainFragment : BaseFragment(TransitionType.INSIDE) {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var mainNavController: NavController
    private lateinit var calculatorNavController: NavController

    protected abstract fun getLayoutId(): Int
    protected abstract fun drawerLayout(): Int
    protected abstract fun navigationView(): Int
    protected abstract fun getMainNavGraphId(): Int
    protected abstract fun getCalculatorNavGraphId(): Int
    protected abstract fun getMainFragmentId(): Int
    protected abstract fun getCalculatorFragmentId(): Int
    protected abstract fun getUnitConverterFragmentId(): Int
    protected abstract fun getCurrencyConverterFragmentId(): Int
    protected abstract fun getSettingsFragmentId(): Int
    protected abstract fun getAboutAppFragmentId(): Int

    protected open fun mapMenuItemToFragmentId(menuItemId: Int): Int {
        return when (menuItemId) {
            R.id.calculator -> getCalculatorFragmentId()
            R.id.unit_converter -> getUnitConverterFragmentId()
            R.id.currency_converter -> getCurrencyConverterFragmentId()
            R.id.settings -> getSettingsFragmentId()
            R.id.about_app -> getAboutAppFragmentId()
            else -> 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawerLayout = view.findViewById(drawerLayout())
        navigationView = view.findViewById(navigationView())

        setupNavigation(view)
    }

    private fun setupNavigation(view: View) {
        mainNavController = findNavController()

        val calculatorNavHostFragment = childFragmentManager
            .findFragmentById(R.id.fragment_container_main_fragment_fc) as NavHostFragment
        calculatorNavController = calculatorNavHostFragment.navController

        setupNavGraphs()

        view.findViewById<MaterialToolbar>(R.id.calculator_top_app_bar_main_fragment_tab).setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val destinationId = mapMenuItemToFragmentId(menuItem.itemId)
            var handled = true

            val isCalculatorGroupItem = when (destinationId) {
                getCalculatorFragmentId(),
                getUnitConverterFragmentId(),
                getCurrencyConverterFragmentId() -> true
                else -> false
            }

            if (isCalculatorGroupItem) {
                if (mainNavController.currentDestination?.id != getMainFragmentId()) {
                    mainNavController.navigate(getMainFragmentId())
                }
                if (calculatorNavController.currentDestination?.id != destinationId) {
                    calculatorNavController.navigate(destinationId)
                }
            } else if (destinationId != 0) {
                if (mainNavController.currentDestination?.id != destinationId) {
                    mainNavController.navigate(destinationId)
                }
            } else {
                handled = false
            }

            if (handled) {
                drawerLayout.close()
            }

            handled
        }

        val destinationChangedListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            updateNavigationViewMenuItems()
        }

        mainNavController.addOnDestinationChangedListener(destinationChangedListener)
        calculatorNavController.addOnDestinationChangedListener(destinationChangedListener)

        view.post { updateNavigationViewMenuItems() }
    }

    private fun setupNavGraphs() {
        val mainGraphId = getMainNavGraphId()
        if (mainGraphId != 0 && mainNavController.graph.id != mainGraphId) {
            val mainNavGraph = mainNavController.navInflater.inflate(mainGraphId)
            mainNavController.graph = mainNavGraph
        }

        val calculatorGraphId = getCalculatorNavGraphId()
        if (calculatorGraphId != 0 && calculatorNavController.graph.id != calculatorGraphId) {
            val calculatorNavGraph = calculatorNavController.navInflater.inflate(calculatorGraphId)
            calculatorNavController.graph = calculatorNavGraph
        }
    }

    private fun updateNavigationViewMenuItems() {
        val currentMainNavDestId = mainNavController.currentDestination?.id
        val currentCalcNavDestId = calculatorNavController.currentDestination?.id


        fun checkMenuItemsRecursive(menu: Menu) {
            menu.forEach { menuItem ->
                if (menuItem.hasSubMenu()) {
                    menuItem.subMenu?.let { checkMenuItemsRecursive(it) }
                } else {
                    if (!menuItem.isCheckable) {
                        return@forEach
                    }

                    val mappedFragmentId = mapMenuItemToFragmentId(menuItem.itemId)
                    var shouldBeChecked = false

                    if (mappedFragmentId != 0){
                        val isCalculatorGroupItem = when (mappedFragmentId) {
                            getCalculatorFragmentId(),
                            getUnitConverterFragmentId(),
                            getCurrencyConverterFragmentId() -> true
                            else -> false
                        }

                        if (isCalculatorGroupItem) {
                            if (currentMainNavDestId == getMainFragmentId() && mappedFragmentId == currentCalcNavDestId) {
                                shouldBeChecked = true
                            }
                        } else {
                            if (mappedFragmentId == currentMainNavDestId) {
                                shouldBeChecked = true
                            }
                        }
                    }

                    if (menuItem.isChecked != shouldBeChecked) {
                        menuItem.isChecked = shouldBeChecked
                    }
                }
            }
        }
        checkMenuItemsRecursive(navigationView.menu)
    }
}