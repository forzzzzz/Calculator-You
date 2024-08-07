package com.forz.calculator.utils

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.forz.calculator.R

object Anim {
    fun buttonAnim(view: View, context: Context){
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.button)
        view.startAnimation(anim)
    }
    fun startVectorAnimation(imageButton: ImageButton, drawable: Int){
        imageButton.setImageResource(drawable)
        val anim = imageButton.drawable as AnimatedVectorDrawable
        anim.start()
    }
}