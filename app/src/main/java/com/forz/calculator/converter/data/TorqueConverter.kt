package com.forz.calculator.converter.data

import com.forz.calculator.R

class TorqueConverter : UnitConverter {
    override val id: Int = 23

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_newton_meter, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilonewton_meter, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_pound_foot, 1.3558179, "${id}12".toInt()),
        FactorUnit(R.string.unit_ounce_inch, 0.08333333, "${id}13".toInt())
    )
}