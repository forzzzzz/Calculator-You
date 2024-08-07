package com.forz.calculator.converter.data


interface ConverterUnit {
     val name: Int
     val id: Int
    fun convertFrom(value: Double): Double
    fun convertTo(value: Double): Double
    fun convert(outputUnit: ConverterUnit, value: Double): Double {
        return outputUnit.convertTo(this.convertFrom(value))
    }
}