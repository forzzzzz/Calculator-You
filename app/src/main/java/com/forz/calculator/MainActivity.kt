package com.forz.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.forz.calculator.StateViews.expressionEditTextIsRecreated
import com.forz.calculator.StateViews.pagerIsRecreated
import com.forz.calculator.StateViews.recyclerViewHistoryIsRecreated
import com.forz.calculator.databinding.ActivityMainBinding
import com.forz.calculator.fragments.MainFragment
import com.forz.calculator.fragments.land.MainLandFragment
import com.forz.calculator.fragments.small.SmallFragment
import com.forz.calculator.fragments.smallLand.SmallLandFragment
import kotlin.properties.Delegates.notNull

@SuppressLint("DiscouragedApi")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding by notNull()
    private var preferences: Preferences by notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)



        if (!preferences.getDynamicColor()){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }


        when (preferences.getTheme()) {
            -1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        showFragment(SmallLandFragment())
                    }
                    else -> {
                        showFragment(SmallFragment())
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        showFragment(MainLandFragment())
                    }
                    else -> {
                        showFragment(MainFragment())
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {

                    }
                    else -> {

                    }
                }
            }
            else -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        showFragment(MainLandFragment())
                    }
                    else -> {
                        showFragment(MainFragment())
                    }
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment){
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main)
        if (currentFragment != null) {
            supportFragmentManager
                .beginTransaction()
                .detach(currentFragment)
                .replace(R.id.main, fragment)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main, fragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        expressionEditTextIsRecreated = true
        pagerIsRecreated = true
        recyclerViewHistoryIsRecreated = true
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_CODE_CHILD) {
            recreate()
        }
    }

    companion object {
        const val REQUEST_CODE_CHILD = 0
    }
}