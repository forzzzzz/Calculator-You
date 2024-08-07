package com.forz.calculator.converter.data

import com.forz.calculator.R

class LightConverter : UnitConverter {
    override val id: Int = 24

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_lumen, 1.0,  "${id}10".toInt()),
        FactorUnit(R.string.unit_candela, 1.0,  "${id}11".toInt()),
        FactorUnit(R.string.unit_lux, 1.0 / 3.14159,  "${id}12".toInt()), // Lumen per square meter
        FactorUnit(R.string.unit_footcandle, 10.7639104167097,  "${id}13".toInt())
    )
}