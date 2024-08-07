package com.forz.calculator.converter.data

import com.forz.calculator.R

class FuelConverter : UnitConverter {
    override val id: Int = 28

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_liter, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_gallon_us, 3.78541, "${id}11".toInt()),
        FactorUnit(R.string.unit_gallon_uk, 4.54609, "${id}12".toInt()),
        FactorUnit(R.string.unit_barrel, 158.98729, "${id}13".toInt())
    )
}