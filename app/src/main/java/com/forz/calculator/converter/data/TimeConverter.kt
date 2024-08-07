package com.forz.calculator.converter.data

import com.forz.calculator.R

class TimeConverter : UnitConverter {
    override val id: Int = 11

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_second, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_minute, 60.0, "${id}11".toInt()),
        FactorUnit(R.string.unit_hour, 3.6E3, "${id}12".toInt()),
        FactorUnit(R.string.unit_day, 8.64E4, "${id}13".toInt()),
        FactorUnit(R.string.unit_week, 6.048E5, "${id}14".toInt())
    )
}