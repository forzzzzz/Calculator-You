package com.forz.calculator.converter.data

import com.forz.calculator.R

class AreaConverter : UnitConverter {
    override val id: Int = 20

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_square_meter, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_square_kilometer, 1E6, "${id}11".toInt()),
        FactorUnit(R.string.unit_square_centimeter, 1E-4, "${id}12".toInt()),
        FactorUnit(R.string.unit_square_millimeter, 1E-7, "${id}13".toInt()),
        FactorUnit(R.string.unit_hectare, 1E4, "${id}14".toInt()),
        FactorUnit(R.string.unit_acre, 4046.8564224, "${id}15".toInt()),
        FactorUnit(R.string.unit_square_yard, 0.83612736, "${id}16".toInt()),
        FactorUnit(R.string.unit_square_foot, 0.09290304, "${id}17".toInt())
    )
}