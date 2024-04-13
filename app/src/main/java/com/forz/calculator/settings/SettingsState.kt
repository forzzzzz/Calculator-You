package com.forz.calculator.settings

import kotlin.properties.Delegates.notNull

object SettingsState {
    var theme: Int by notNull()
    var color: String by notNull()
    var isDynamicColor: Boolean by notNull()

    var groupingSeparatorSymbol: String by notNull()
    var previousGroupingSeparatorSymbol: String by notNull()
    var decimalSeparatorSymbol: String by notNull()
    var previousDecimalSeparatorSymbol: String by notNull()
    var numberPrecision: Int by notNull()

    var vibration: Boolean by notNull()
    var sound: Boolean by notNull()



    fun init(
            theme: Int,
            color: String,
            isDynamicColor: Boolean,
            groupingSeparatorSymbol: String,
            decimalSeparatorSymbol: String,
            numberPrecision: Int,
            vibration: Boolean,
            sound: Boolean
    ){

        SettingsState.theme = theme
        SettingsState.color = color
        SettingsState.isDynamicColor = isDynamicColor

        SettingsState.groupingSeparatorSymbol = groupingSeparatorSymbol
        previousGroupingSeparatorSymbol = groupingSeparatorSymbol
        SettingsState.decimalSeparatorSymbol = decimalSeparatorSymbol
        previousDecimalSeparatorSymbol = decimalSeparatorSymbol
        SettingsState.numberPrecision = numberPrecision

        SettingsState.vibration = vibration
        SettingsState.sound = sound
    }
}