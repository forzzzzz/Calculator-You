package com.forz.calculator.converter.data

import com.forz.calculator.R

class PressureConverter : UnitConverter {
    override val id: Int = 18

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_pascal, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilopascal, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_megapascal, 1E6, "${id}12".toInt()),
        FactorUnit(R.string.unit_gigapascal, 1E9, "${id}13".toInt()),
        FactorUnit(R.string.unit_bar, 1E5, "${id}14".toInt()),
        FactorUnit(R.string.unit_millibar, 1E2, "${id}15".toInt()),
        FactorUnit(R.string.unit_atmosphere, 101325.0, "${id}16".toInt()),
        FactorUnit(R.string.unit_psi, 6894.757293168, "${id}17".toInt()), // Pounds per Square Inch
        FactorUnit(R.string.unit_h2o, 249.082, "${id}18".toInt()), // Inches of water
    )
}