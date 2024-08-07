package com.forz.calculator.converter.data

import com.forz.calculator.R

class DensityConverter : UnitConverter {
    override val id: Int = 21

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_kilogram_per_cubic_meter, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_gram_per_cubic_centimeter, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_pound_per_cubic_foot, 16.018463, "${id}12".toInt()),
        FactorUnit(R.string.unit_pound_per_gallon, 0.11982642, "${id}13".toInt())
    )
}