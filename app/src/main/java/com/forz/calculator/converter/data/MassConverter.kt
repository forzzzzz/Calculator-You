package com.forz.calculator.converter.data

import com.forz.calculator.R

class MassConverter : UnitConverter {
    override val id: Int = 12

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_kilogram, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_gram, 1E-3, "${id}11".toInt()),
        FactorUnit(R.string.unit_milligram, 1E-6, "${id}12".toInt()),
        FactorUnit(R.string.unit_tonne, 1E3, "${id}13".toInt()),
        FactorUnit(R.string.unit_pound, 0.45359237, "${id}14".toInt()),
        FactorUnit(R.string.unit_ounce, 0.028349523125, "${id}15".toInt())
    )
}