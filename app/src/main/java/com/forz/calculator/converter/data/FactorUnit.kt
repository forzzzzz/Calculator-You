package com.forz.calculator.converter.data

import androidx.annotation.StringRes

class FactorUnit(@StringRes override val name: Int, private val conversionFactor: Double, override val id: Int) :
    ConverterUnit {
    override fun convertFrom(value: Double): Double {
        return value * conversionFactor
    }

    override fun convertTo(value: Double): Double {
        return value / conversionFactor
    }
}
