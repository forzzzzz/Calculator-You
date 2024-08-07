package com.forz.calculator.converter.data

import com.forz.calculator.R

class VolumeConverter : UnitConverter {
    override val id: Int = 19

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_liter, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_milliliter, 1E-3, "${id}11".toInt()),
        FactorUnit(R.string.unit_kiloliter, 1E3, "${id}12".toInt()),
        FactorUnit(R.string.unit_cubic_meter, 1E3, "${id}13".toInt()),
        FactorUnit(R.string.unit_cubic_centimeter, 1E-3, "${id}14".toInt()),
        FactorUnit(R.string.unit_cubic_decimeter, 1.0, "${id}15".toInt()),
        FactorUnit(R.string.unit_gallon, 3.785411784, "${id}16".toInt()),
        FactorUnit(R.string.unit_quart, 0.946352946, "${id}17".toInt()),
        FactorUnit(R.string.unit_pint, 0.473176473, "${id}18".toInt()),
        FactorUnit(R.string.unit_fluid_ounce, 0.02957352957, "${id}19".toInt()),
        FactorUnit(R.string.unit_tablespoon, 0.01478676479, "${id}20".toInt()),
        FactorUnit(R.string.unit_teaspoon, 0.00492892159, "${id}21".toInt())
    )
}