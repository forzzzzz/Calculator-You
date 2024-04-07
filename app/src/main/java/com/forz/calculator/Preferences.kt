package com.forz.calculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Vibrator
import com.google.android.material.color.utilities.DynamicColor

class Preferences(context: Context) {

    companion object {
        private const val THEME = "marktka.calculatorYou.THEME"

        private const val EXPRESSION = "marktka.calculatorYou.EXPRESSION"
        private const val CURSOR_POSITION = "marktka.calculatorYou.CURSOR_POSITION"

        private const val VIBRATION_STATUS = "marktka.calculatorYou.VIBRATION_STATUS"
        private const val SOUND_EFFECTS_STATUS = "marktka.calculatorYou.SOUND_EFFECTS_STATUS"
        private const val DYNAMIC_COLORS = "marktka.calculatorYou.DYNAMIC_COLORS"
        private const val COLOR = "marktka.calculatorYou.COLOR"

        private const val NUMBER_PRECISION = "marktka.calculatorYou.NUMBER_PRECISION"
        private const val GROUPING_SEPARATOR_SYMBOL = "marktka.calculatorYou.GROUPING_SEPARATOR_SYMBOL"
        private const val DECIMAL_SEPARATOR_SYMBOL = "marktka.calculatorYou.DECIMAL_SEPARATOR_SYMBOL"

        private const val DEGREE_MOD = "marktka.calculatorYou.DEGREE_MOD"


        private const val FORCE_DAY_NIGHT = "darkempire78.calculatorYou.FORCE_DAY_NIGHT"


        private const val KEY_HISTORY = "marktka.calculatorYou.HISTORY"
        private const val KEY_PREVENT_PHONE_FROM_SLEEPING = "marktka.calculatorYou.PREVENT_PHONE_FROM_SLEEPING"
        private const val KEY_HISTORY_SIZE = "marktka.calculatorYou.HISTORY_SIZE"
        private const val KEY_SCIENTIFIC_MODE_ENABLED_BY_DEFAULT = "marktka.calculatorYou.SCIENTIFIC_MODE_ENABLED_BY_DEFAULT"
        private const val KEY_RADIANS_INSTEAD_OF_DEGREES_BY_DEFAULT = "marktka.calculatorYou.RADIANS_INSTEAD_OF_DEGREES_BY_DEFAULT"
        private const val KEY_NUMBER_PRECISION = "marktka.calculatorYou.NUMBER_PRECISION"
        private const val KEY_WRITE_NUMBER_INTO_SCIENTIC_NOTATION = "marktka.calculatorYou.WRITE_NUMBER_INTO_SCIENTIC_NOTATION"
        private const val KEY_LONG_CLICK_TO_COPY_VALUE = "marktka.calculatorYou.LONG_CLICK_TO_COPY_VALUE"
        private const val KEY_ADD_MODULO_BUTTON = "marktka.calculatorYou.ADD_MODULO_BUTTON"
        private const val KEY_SPLIT_PARENTHESIS_BUTTON = "marktka.calculatorYou.SPLIT_PARENTHESIS_BUTTON"
    }


    private val  preferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    fun setTheme(input: Int) {
        val editor = preferences.edit()
        editor.putInt(THEME, input)
        editor.apply()
    }

    fun setExpression(input: String){
        val editor = preferences.edit()
        editor.putString(EXPRESSION, input)
        editor.apply()
    }

    fun setCursorPosition(input: Int){
        val editor = preferences.edit()
        editor.putInt(CURSOR_POSITION, input)
        editor.apply()
    }

    fun setVibration(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(VIBRATION_STATUS, input)
        editor.apply()
    }

    fun setSoundEffects(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(SOUND_EFFECTS_STATUS, input)
        editor.apply()
    }

    fun setDynamicColor(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(DYNAMIC_COLORS, input)
        editor.apply()
    }

    fun setColor(input: String){
        val editor = preferences.edit()
        editor.putString(COLOR, input)
        editor.apply()
    }

    fun setNumberPrecision(input: Int){
        val editor = preferences.edit()
        editor.putInt(NUMBER_PRECISION, input)
        editor.apply()
    }

    fun setGroupingSeparatorSymbol(input: String){
        val editor = preferences.edit()
        editor.putString(GROUPING_SEPARATOR_SYMBOL, input)
        editor.apply()
    }

    fun setDecimalSeparatorSymbol(input: String){
        val editor = preferences.edit()
        editor.putString(DECIMAL_SEPARATOR_SYMBOL, input)
        editor.apply()
    }

    fun setDegreeMod(input: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(DEGREE_MOD, input)
        editor.apply()
    }







    fun getTheme(): Int{
        return preferences.getInt(THEME, -1)
    }

    fun getExpression(): String{
        return preferences.getString(EXPRESSION, "").toString()
    }

    fun getCursorPosition(): Int{
        return preferences.getInt(CURSOR_POSITION, 0)
    }

    fun getVibration(): Boolean{
        return preferences.getBoolean(VIBRATION_STATUS, true)
    }

    fun getSoundEffects(): Boolean{
        return preferences.getBoolean(SOUND_EFFECTS_STATUS, true)
    }

    fun getDynamicColor(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            preferences.getBoolean(DYNAMIC_COLORS, true)
        else
            preferences.getBoolean(DYNAMIC_COLORS, false)
    }

    fun getColor(): String{
        return preferences.getString(COLOR, "com.marktka.calculatorYou:id/color_0").toString()
    }

    fun getNumberPrecision(): Int{
        return preferences.getInt(NUMBER_PRECISION, 10)
    }

    fun getGroupingSeparatorSymbol(): String{
        return preferences.getString(GROUPING_SEPARATOR_SYMBOL, ",").toString()
    }

    fun getDecimalSeparatorSymbol(): String{
        return preferences.getString(DECIMAL_SEPARATOR_SYMBOL, ".").toString()
    }

    fun getDegreeMod(): Boolean{
        return preferences.getBoolean(DEGREE_MOD, true)
    }
}