package com.forz.calculator.converter.data

import com.forz.calculator.R

class SpeedConverter : UnitConverter {
    override val id: Int = 14

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_meter_per_second, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilometer_per_hour, 0.277777778, "${id}11".toInt()),
        FactorUnit(R.string.unit_mile_per_hour, 0.44704, "${id}12".toInt()),
        FactorUnit(R.string.unit_knot, 0.514444444, "${id}13".toInt()),
        FactorUnit(R.string.unit_feet_per_second, 0.3048, "${id}14".toInt())
    )
}