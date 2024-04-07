package com.forz.calculator.history

import java.time.LocalDate

data class HistoryData(
    val id: Int,
    val expression: String,
    val result: String,
    val date: LocalDate
)
