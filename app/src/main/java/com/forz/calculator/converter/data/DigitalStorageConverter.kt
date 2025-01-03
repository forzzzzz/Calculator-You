package com.forz.calculator.converter.data

import com.forz.calculator.R

class DigitalStorageConverter : UnitConverter {
    override val id: Int = 25

    override val units: List<ConverterUnit> = listOf(
        FactorUnit(R.string.unit_bit, 1.0, "${id}10".toInt()),
        FactorUnit(R.string.unit_byte, 8.0, "${id}11".toInt()),
        FactorUnit(R.string.unit_kilobit, 1E3, "${id}12".toInt()),
        FactorUnit(R.string.unit_kilobyte, 8E3, "${id}13".toInt()),
        FactorUnit(R.string.unit_kibibyte, 8.0 * 1024, "${id}14".toInt()),
        FactorUnit(R.string.unit_megabit, 1E6, "${id}15".toInt()),
        FactorUnit(R.string.unit_megabyte, 8E6, "${id}16".toInt()),
        FactorUnit(R.string.unit_mebibyte, 8.0 * 1024 * 1024, "${id}17".toInt()),
        FactorUnit(R.string.unit_gigabit, 1E9, "${id}18".toInt()),
        FactorUnit(R.string.unit_gigabyte, 8E9, "${id}19".toInt()),
        FactorUnit(R.string.unit_gigibyte, 8.0 * 1024 * 1024 * 1024, "${id}20".toInt()),
        FactorUnit(R.string.unit_terabit, 1E12, "${id}21".toInt()),
        FactorUnit(R.string.unit_terabyte, 8E12, "${id}22".toInt()),
        FactorUnit(R.string.unit_tebibyte, 8.0 * 1024 * 1024 * 1024 * 1024, "${id}23".toInt()),
        FactorUnit(R.string.unit_petabit, 1E15, "${id}24".toInt()),
        FactorUnit(R.string.unit_petabyte, 8E15, "${id}25".toInt()),
        FactorUnit(R.string.unit_pebibyte, 8.0 * 1024 * 1024 * 1024 * 1024 * 1024, "${id}26".toInt()),
        FactorUnit(R.string.unit_exabit, 1E18, "${id}27".toInt()),
        FactorUnit(R.string.unit_exabyte, 8E18, "${id}28".toInt()),
        FactorUnit(R.string.unit_exbibyte, 8.0 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024, "${id}29".toInt())
    )
}