package com.forz.calculator.old_arch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


interface OnMainActivityListener {
    fun onBackPressed(): Boolean
}

@Suppress("DEPRECATION")
@SuppressLint("DiscouragedApi")
class MainActivity : AppCompatActivity() {

//    private var binding: ActivityMainBinding by notNull()
//    private var preferences: Preferences by notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




//        preferences = Preferences(this)
//
//
//        Config.init(
//            preferences.getTheme(),
//            preferences.getColor(),
//            preferences.getDynamicColor(),
//            preferences.getGroupingSeparatorSymbol(),
//            preferences.getDecimalSeparatorSymbol(),
//            preferences.getNumberPrecision(),
//            preferences.getMaxScientificNotationDigits(),
//            preferences.getSwipeHistoryAndCalculator(),
//            preferences.getSwipeDigitsAndScientificFunctions(),
//            preferences.getAutoSavingResults(),
//            preferences.getVibration(),
//            preferences.getSoundEffects()
//        )
//        CalculatorViewModel.init(preferences.getDegreeMod())
//        UnitConverterFragment.physicalQuantity = preferences.getPhysicalQuantity()
//        UnitConverterFragment.unit = preferences.getUnit()
//
//
//        if (!preferences.getDynamicColor()){
//            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
//        }else{
//            setTheme(R.style.dynamicColors)
//        }
//
//
//        when (preferences.getTheme()) {
//            -1 -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            }
//            1 -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            2 -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//        }
//
//
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
//
//
//        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
//            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        showFragment(SmallLandFragment())
//                    }
//                    else -> {
//                        showFragment(SmallFragment())
//                    }
//                }
//            }
//            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        showFragment(LargeLandFragment())
//                    }
//                    else -> {
//                        showFragment(MainFragment())
//                    }
//                }
//            }
//            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        showFragment(XLargeLandFragment())
//                    }
//                    else -> {
//                        showFragment(MainFragment())
//                    }
//                }
//            }
//            else -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        showFragment(MainLandFragment())
//                    }
//                    else -> {
//                        showFragment(MainFragment())
//                    }
//                }
//            }
//        }
//
//        Evaluator.converterResult.observe(this){
//            val componentName = ComponentName(this, MyQSTileService::class.java)
//            TileService.requestListeningState(this, componentName)
//        }
//    }
//
//    private fun showFragment(fragment: Fragment){
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.main)
//        if (currentFragment != null) {
//            supportFragmentManager
//                .beginTransaction()
//                .detach(currentFragment)
//                .replace(R.id.main, fragment)
//                .commit()
//        }else{
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.main, fragment)
//                .commit()
//        }
    }

    override fun onStop() {
        super.onStop()

//        preferences.setDegreeMod(CalculatorViewModel.isDegreeModActivated.value!!)
//        preferences.setPhysicalQuantity(UnitConverterFragment.physicalQuantity)
//        preferences.setUnit(UnitConverterFragment.unit)
    }

    override fun onDestroy() {
        super.onDestroy()

//        recyclerViewHistoryIsRecreated = true
    }


//    override fun onBackPressed() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.main)
//        if (fragment is OnMainActivityListener) {
//            val handled = (fragment as OnMainActivityListener).onBackPressed()
//            if (!handled) {
//                super.onBackPressed()
//            }
//        } else {
//            super.onBackPressed()
//        }
//    }



//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == REQUEST_CODE_CHILD) {
//            firstCreatedSettingsActivity = true
//            recreate()
//        }
//    }

    companion object {
        const val REQUEST_CODE_CHILD = 0
    }
}