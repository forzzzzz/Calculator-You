package com.forz.calculator.converter.data

import com.forz.calculator.R

class PowerConverter : UnitConverter {
    override val id: Int = 17

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_watt, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilowatt, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_megawatt, 1E6, "${id}12".toInt()),
        FactorUnit(R.string.unit_horsepower, 745.7, "${id}13".toInt()),
        FactorUnit(R.string.unit_btu_per_hour, 252.0, "${id}14".toInt())
    )
}