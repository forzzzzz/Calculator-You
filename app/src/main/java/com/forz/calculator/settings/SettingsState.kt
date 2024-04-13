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

    var swipeHistoryAndCalculator: Boolean by notNull()
    var swipeDigitsAndScientificFunctions: Boolean by notNull()
    var vibration: Boolean by notNull()
    var sound: Boolean by notNull()



    fun init(
            theme: Int,
            color: String,
            isDynamicColor: Boolean,
            groupingSeparatorSymbol: String,
            decimalSeparatorSymbol: String,
            numberPrecision: Int,
            swipeHistoryAndCalculator: Boolean,
            swipeDigitsAndScientificFunctions: Boolean,
            vibration: Boolean,
            sound: Boolean
    ){

        this.theme = theme
        this.color = color
        this.isDynamicColor = isDynamicColor

        this.groupingSeparatorSymbol = groupingSeparatorSymbol
        previousGroupingSeparatorSymbol = groupingSeparatorSymbol
        this.decimalSeparatorSymbol = decimalSeparatorSymbol
        previousDecimalSeparatorSymbol = decimalSeparatorSymbol
        this.numberPrecision = numberPrecision

        this.swipeHistoryAndCalculator = swipeHistoryAndCalculator
        this.swipeDigitsAndScientificFunctions = swipeDigitsAndScientificFunctions
        this.vibration = vibration
        this.sound = sound
    }
}