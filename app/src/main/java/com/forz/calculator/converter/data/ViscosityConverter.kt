package com.forz.calculator.converter.data

import com.forz.calculator.R

class ViscosityConverter : UnitConverter {
    override val id: Int = 26

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_pascal_second, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_centipoise, 0.01, "${id}11".toInt()),
        FactorUnit(R.string.unit_stoke, 1E4, "${id}12".toInt()),
        FactorUnit(R.string.unit_poiseuille, 1E9, "${id}13".toInt())
    )
}