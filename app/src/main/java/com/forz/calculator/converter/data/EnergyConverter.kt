package com.forz.calculator.converter.data

import com.forz.calculator.R

class EnergyConverter : UnitConverter {
    override val id: Int = 16

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_joule, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_kilojoule, 1E3, "${id}11".toInt()),
        FactorUnit(R.string.unit_megajoule, 1E6, "${id}12".toInt()),
        FactorUnit(R.string.unit_calorie, 4.184, "${id}13".toInt()),
        FactorUnit(R.string.unit_kilocalorie, 4.184E3, "${id}14".toInt()),
        FactorUnit(R.string.unit_grams_of_carbohydrate, 4.0 * 4.184, "${id}15".toInt()),
        FactorUnit(R.string.unit_grams_of_fat, 9.0 * 4.184, "${id}16".toInt()),
        FactorUnit(R.string.unit_grams_of_protein, 4.0 * 4.184, "${id}17".toInt())
    )
}