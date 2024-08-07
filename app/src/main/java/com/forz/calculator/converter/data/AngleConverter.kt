package com.forz.calculator.converter.data

import com.forz.calculator.R

class AngleConverter : UnitConverter {
    override val id: Int = 27

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_degree, Math.PI / 180, "${id}10".toInt()),
        FactorUnit(R.string.unit_radian, 1.0, "${id}11".toInt()),
        FactorUnit(R.string.unit_gradian, Math.PI / 200, "${id}12".toInt()),
        FactorUnit(R.string.unit_turn, 2 * Math.PI, "${id}13".toInt())
    )
}

