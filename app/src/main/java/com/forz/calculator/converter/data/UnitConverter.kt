package com.forz.calculator.converter.data

interface UnitConverter {
    val id: Int
    val units: List<ConverterUnit>

    fun convertAll(value: Double, unit: ConverterUnit): List<Pair<ConverterUnit, Double>> {
        return units.map {
            it to unit.convert(it, value)
        }
    }
}