package com.forz.calculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class Preferences(context: Context) {

    companion object {
        private const val KEY_THEME = "marktka.calculatorYou.THEME"

        private const val KEY_VIBRATION_STATUS = "marktka.calculatorYou.VIBRATION_STATUS"
        private const val KEY_SOUND_EFFECTS_STATUS = "marktka.calculatorYou.SOUND_EFFECTS_STATUS"
        private const val KEY_DYNAMIC_COLORS = "marktka.calculatorYou.DYNAMIC_COLORS"
        private const val KEY_COLOR = "marktka.calculatorYou.COLOR"

        private const val KEY_NUMBER_PRECISION = "marktka.calculatorYou.NUMBER_PRECISION"
        private const val KEY_GROUPING_SEPARATOR_SYMBOL = "marktka.calculatorYou.GROUPING_SEPARATOR_SYMBOL"
        private const val KEY_DECIMAL_SEPARATOR_SYMBOL = "marktka.calculatorYou.DECIMAL_SEPARATOR_SYMBOL"

        private const val KEY_DEGREE_MOD = "marktka.calculatorYou.DEGREE_MOD"

        private const val KEY_SWIPE_HISTORY_AND_CALCULATOR = "marktka.calculatorYou.SWIPE_HISTORY_AND_CALCULATOR"
        private const val KEY_SWIPE_DIGITS_AND_SCIENTIFIC_FUNCTIONS = "marktka.calculatorYou.SWIPE_DIGITS_AND_SCIENTIFIC_FUNCTIONS"
    }


    private val preferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    fun setTheme(input: Int) {
        val editor = preferences.edit()
        editor.putInt(KEY_THEME, input)
        editor.apply()
    }

    fun setVibration(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_VIBRATION_STATUS, input)
        editor.apply()
    }

    fun setSoundEffects(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_SOUND_EFFECTS_STATUS, input)
        editor.apply()
    }

    fun setDynamicColor(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_DYNAMIC_COLORS, input)
        editor.apply()
    }

    fun setColor(input: String){
        val editor = preferences.edit()
        editor.putString(KEY_COLOR, input)
        editor.apply()
    }

    fun setNumberPrecision(input: Int){
        val editor = preferences.edit()
        editor.putInt(KEY_NUMBER_PRECISION, input)
        editor.apply()
    }

    fun setGroupingSeparatorSymbol(input: String){
        val editor = preferences.edit()
        editor.putString(KEY_GROUPING_SEPARATOR_SYMBOL, input)
        editor.apply()
    }

    fun setDecimalSeparatorSymbol(input: String){
        val editor = preferences.edit()
        editor.putString(KEY_DECIMAL_SEPARATOR_SYMBOL, input)
        editor.apply()
    }

    fun setDegreeMod(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_DEGREE_MOD, input)
        editor.apply()
    }

    fun setSwipeHistoryAndCalculator(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_SWIPE_HISTORY_AND_CALCULATOR, input)
        editor.apply()
    }

    fun setSwipeDigitsAndScientificFunctions(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(KEY_SWIPE_DIGITS_AND_SCIENTIFIC_FUNCTIONS, input)
        editor.apply()
    }







    fun getTheme(): Int{
        return preferences.getInt(KEY_THEME, -1)
    }

    fun getVibration(): Boolean{
        return preferences.getBoolean(KEY_VIBRATION_STATUS, true)
    }

    fun getSoundEffects(): Boolean{
        return preferences.getBoolean(KEY_SOUND_EFFECTS_STATUS, true)
    }

    fun getDynamicColor(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            preferences.getBoolean(KEY_DYNAMIC_COLORS, true)
        else
            preferences.getBoolean(KEY_DYNAMIC_COLORS, false)
    }

    fun getColor(): String{
        return preferences.getString(KEY_COLOR, "color_0").toString()
    }

    fun getNumberPrecision(): Int{
        return preferences.getInt(KEY_NUMBER_PRECISION, 10)
    }

    fun getGroupingSeparatorSymbol(): String{
        return preferences.getString(KEY_GROUPING_SEPARATOR_SYMBOL, ",").toString()
    }

    fun getDecimalSeparatorSymbol(): String{
        return preferences.getString(KEY_DECIMAL_SEPARATOR_SYMBOL, ".").toString()
    }

    fun getDegreeMod(): Boolean{
        return preferences.getBoolean(KEY_DEGREE_MOD, true)
    }

    fun getSwipeHistoryAndCalculator(): Boolean{
        return preferences.getBoolean(KEY_SWIPE_HISTORY_AND_CALCULATOR, true)
    }

    fun getSwipeDigitsAndScientificFunctions(): Boolean{
        return preferences.getBoolean(KEY_SWIPE_DIGITS_AND_SCIENTIFIC_FUNCTIONS, true)
    }
}