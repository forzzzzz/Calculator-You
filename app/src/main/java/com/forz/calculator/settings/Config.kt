package com.forz.calculator.settings

import kotlin.properties.Delegates.notNull

object Config {
    var theme: Int by notNull()
    var color: String by notNull()
    var isDynamicColor: Boolean by notNull()

    var groupingSeparatorSymbol: String by notNull()
    var oldGroupingSeparatorSymbol: String by notNull()
    var decimalSeparatorSymbol: String by notNull()
    var oldDecimalSeparatorSymbol: String by notNull()
    var numberPrecision: Int by notNull()
    var maxScientificNotationDigits: Int by notNull()

    var swipeMain: Boolean by notNull()
    var swipeDigitsAndScientificFunctions: Boolean by notNull()
    var autoSavingResults: Boolean by notNull()
    var vibration: Boolean by notNull()
    var sound: Boolean by notNull()



    fun init(
            theme: Int,
            color: String,
            isDynamicColor: Boolean,
            groupingSeparatorSymbol: String,
            decimalSeparatorSymbol: String,
            numberPrecision: Int,
            maxIntegerDigits: Int,
            swipeHistoryAndCalculator: Boolean,
            swipeDigitsAndScientificFunctions: Boolean,
            savingIntermediateResults: Boolean,
            vibration: Boolean,
            sound: Boolean
    ){

        this.theme = theme
        this.color = color
        this.isDynamicColor = isDynamicColor

        this.groupingSeparatorSymbol = groupingSeparatorSymbol
        oldGroupingSeparatorSymbol = groupingSeparatorSymbol
        this.decimalSeparatorSymbol = decimalSeparatorSymbol
        oldDecimalSeparatorSymbol = decimalSeparatorSymbol
        this.numberPrecision = numberPrecision
        this.maxScientificNotationDigits = maxIntegerDigits

        this.swipeMain = swipeHistoryAndCalculator
        this.swipeDigitsAndScientificFunctions = swipeDigitsAndScientificFunctions
        this.autoSavingResults = savingIntermediateResults
        this.vibration = vibration
        this.sound = sound
    }
}