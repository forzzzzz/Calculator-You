package com.forz.calculator.old_arch.history

import java.time.LocalDate

data class HistoryData(
    val id: Int,
    val expression: String,
    val result: String,
    val date: LocalDate
)
