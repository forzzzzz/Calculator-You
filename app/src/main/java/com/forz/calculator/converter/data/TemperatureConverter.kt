package com.forz.calculator.converter.data

import androidx.annotation.StringRes
import com.forz.calculator.R

class TemperatureConverter : UnitConverter {
    override val id: Int = 13

    override val units: List<ConverterUnit> = listOf(
        object : ConverterUnit {
            @StringRes
            override val name: Int = R.string.unit_celsius
            override val id: Int = "${this@TemperatureConverter.id}10".toInt()

            override fun convertFrom(value: Double): Double {
                return value
            }

            override fun convertTo(value: Double): Double {
                return value
            }
        },
        object : ConverterUnit {
            @StringRes
            override val name: Int = R.string.unit_fahrenheit
            override val id: Int = "${this@TemperatureConverter.id}11".toInt()

            override fun convertFrom(value: Double): Double {
                return (value - 32) / 1.8
            }

            override fun convertTo(value: Double): Double {
                return (value * 1.8) + 32
            }
        },
        object : ConverterUnit {
            @StringRes
            override val name: Int = R.string.unit_kelvin
            override val id: Int = "${this@TemperatureConverter.id}12".toInt()

            override fun convertFrom(value: Double): Double {
                return value - 273.15
            }

            override fun convertTo(value: Double): Double {
                return value + 273.15
            }
        },
    )
}