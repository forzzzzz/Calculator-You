package com.forz.calculator

import android.app.Application
import com.forz.calculator.history.HistoryService

class App: Application() {
    val historyService: HistoryService by lazy {
        HistoryService(this)
    }
}