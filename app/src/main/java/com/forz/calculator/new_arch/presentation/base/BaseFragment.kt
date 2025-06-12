package com.forz.calculator.new_arch.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

abstract class BaseFragment(
    private val transitionType: TransitionType = TransitionType.INSIDE
): Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (transitionType) {
            TransitionType.NONE -> {
                enterTransition = null
                exitTransition = null
                reenterTransition = null
                returnTransition = null
            }
            TransitionType.INSIDE -> {
                enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
                returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            }
            TransitionType.FADE -> {
                MaterialFadeThrough().let {
                    enterTransition = it
                    exitTransition = it
                }
            }
        }
    }
}