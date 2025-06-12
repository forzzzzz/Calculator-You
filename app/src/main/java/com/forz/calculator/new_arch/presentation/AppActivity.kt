package com.forz.calculator.new_arch.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.forz.calculator.R
import com.forz.calculator.databinding.ActivityAppBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.app_activity_main_container) as NavHostFragment
        val navController = navHostFragment.navController

        val graphId = when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        R.navigation.nav_graph_small_land
                    }
                    else -> {
//                        R.navigation.nav_graph_small
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        R.navigation.nav_graph_large_land
                    }
                    else -> {
//                        R.navigation.nav_graph_main
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        R.navigation.nav_graph_xlarge_land
                    }
                    else -> {
//                        R.navigation.nav_graph_main
                    }
                }
            }
            else -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        R.navigation.nav_graph_main_land
                    }
                    else -> {
                        R.navigation.main_navigation
                    }
                }
            }
        }

        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)
        navController.graph = navGraph
    }
}