package com.forz.calculator

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

object Anim {
    fun buttonAnim(view: View, context: Context){
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.button)
        view.startAnimation(anim)
    }
}