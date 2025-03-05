package com.forz.calculator.old_arch

import android.app.Application
import com.forz.calculator.old_arch.history.HistoryService

class App: Application() {






    val historyService: HistoryService by lazy {
        HistoryService(this)
    }
}