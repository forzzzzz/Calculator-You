package com.forz.calculator.converter.data

import com.forz.calculator.R

class FrequencyConverter : UnitConverter {
    override val id: Int = 22

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_hertz, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilohertz, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_megahertz, 1E6, "${id}12".toInt()),
        FactorUnit(R.string.unit_gigahertz, 1E9, "${id}13".toInt())
    )
}