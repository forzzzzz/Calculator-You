package com.forz.calculator.history

import android.content.Context
import java.time.LocalDate

typealias HistoryDataListListener = (historyDataList: List<HistoryData>) -> Unit

class HistoryService(context: Context) {
    private var historyDataList = mutableListOf<HistoryData>()
    private val listeners = mutableSetOf<HistoryDataListListener>()
    private val dbHelper = DBHelper(context)

    init {
        val data = dbHelper.selectAllOrderByDate()
        historyDataList.addAll(data)
    }


    fun addHistoryData(expression: String, result: String){
        val id = if (historyDataList.isNotEmpty()) historyDataList.last().id + 1 else 1
        val historyData = HistoryData(
            id = id,
            expression = expression,
            result = result,
            date = LocalDate.now())

        historyDataList = ArrayList(historyDataList)
        historyDataList.add(historyData)
        dbHelper.insertData(historyData.expression, historyData.result)
        notifyChanges()
    }

    fun deleteHistoryData(indexToDelete: Int){
        historyDataList.removeIf {it.id == indexToDelete}
        dbHelper.deleteById(indexToDelete)
        notifyChanges()
    }

    fun clearHistoryData(){
        historyDataList = ArrayList(historyDataList)
        historyDataList.clear()
        dbHelper.clearTable()
        notifyChanges()
    }


    fun addListener(listener: HistoryDataListListener){
        listeners.add(listener)
        listener.invoke(historyDataList)
    }

    fun removeListener(listener: HistoryDataListListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach {it.invoke(historyDataList)}
    }
}