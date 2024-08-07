package com.forz.calculator.converter.data

import com.forz.calculator.R

class LengthConverter : UnitConverter {
    override val id: Int = 10

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_meter, 1.0,  "${id}10".toInt()),
        FactorUnit(R.string.unit_kilometer, 1E3,  "${id}11".toInt()),
        FactorUnit(R.string.unit_centimeter, 1E-2, "${id}12".toInt()),
        FactorUnit(R.string.unit_millimeter, 1E-3, "${id}13".toInt()),
        FactorUnit(R.string.unit_mile, 1609.344, "${id}14".toInt()),
        FactorUnit(R.string.unit_yard, 0.9144, "${id}15".toInt()),
        FactorUnit(R.string.unit_foot, 0.3048, "${id}16".toInt()),
        FactorUnit(R.string.unit_inch, 0.0254, "${id}17".toInt()),
        FactorUnit(R.string.unit_nautical_mile, 1852.0, "${id}18".toInt())
    )
}
