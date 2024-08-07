package com.forz.calculator.converter.data

import com.forz.calculator.R

class ForceConverter : UnitConverter {
    override val id: Int = 15

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_newton, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilonewton, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_dyne, 1E-5, "${id}12".toInt()),
        FactorUnit(R.string.unit_pound_force, 4.448222, "${id}13".toInt()),
        FactorUnit(R.string.unit_ounce_force, 0.2780139, "${id}14".toInt())
    )
}