package com.forz.calculator.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import com.forz.calculator.settings.Config.sound
import com.forz.calculator.settings.Config.vibration

@Suppress("DEPRECATION")
class HapticAndSound(context: Context, private val views: Array<View>) {
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrateEffectClick() {
        if (shouldPerformHapticFeedback()) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.cancel()
            vibrator.vibrate(vibrationEffect)
        }
    }

    fun vibrateEffectHeavyClick() {
        if (shouldPerformHapticFeedback()) {
            val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
            vibrator.cancel()
            vibrator.vibrate(vibrationEffect)
        }
    }


    fun setSoundEffects() {
        if (sound){
            for (view in views) {
                view.isSoundEffectsEnabled = true
            }
        }else{
            for (view in views) {
                view.isSoundEffectsEnabled = false
            }
        }
    }

    fun setHapticFeedback() {
        if (vibration){
            for (view in views) {
                view.isHapticFeedbackEnabled = true
            }
        }else{
            for (view in views) {
                view.isHapticFeedbackEnabled = false
            }
        }

    }

    private fun shouldPerformHapticFeedback(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && vibrator.hasVibrator() && vibration
    }
}