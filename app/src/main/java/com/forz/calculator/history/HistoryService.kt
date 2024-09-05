package com.forz.calculator.history

import android.content.Context
import com.forz.calculator.utils.NumberFormatter.changeSeparatorsExpression
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
        if (historyDataList.isEmpty() || expression != historyDataList.first().expression){
            val id = if (historyDataList.isNotEmpty()) historyDataList.first().id + 1 else 1
            val historyData = HistoryData(
                id = id,
                expression = expression,
                result = result,
                date = LocalDate.now())

            historyDataList = ArrayList(historyDataList)
            historyDataList.add(0, historyData)
            dbHelper.insertData(historyData.expression, historyData.result)
            notifyChanges()
        }
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

    fun modifyHistoryData(oldGroupingSeparatorSymbol: String, newGroupingSeparatorSymbol: String,
                          oldDecimalSeparatorSymbol: String, newDecimalSeparatorSymbol: String){

        val modifiedList = historyDataList.map { historyData ->
            val newExpression = changeSeparatorsExpression(
                historyData.expression,
                oldGroupingSeparatorSymbol, newGroupingSeparatorSymbol,
                oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol
            )
            val newResult = changeSeparatorsExpression(
                historyData.result,
                oldGroupingSeparatorSymbol, newGroupingSeparatorSymbol,
                oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol
            )
            HistoryData(historyData.id, newExpression, newResult, historyData.date)
        }

        historyDataList = ArrayList(historyDataList)
        historyDataList = modifiedList.toMutableList()
        dbHelper.modifyAllRecords(
            modifyExpression = { expression ->  changeSeparatorsExpression(
                expression,
                oldGroupingSeparatorSymbol, newGroupingSeparatorSymbol,
                oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol
            )},
            modifyResult = { result -> changeSeparatorsExpression(
                result,
                oldGroupingSeparatorSymbol, newGroupingSeparatorSymbol,
                oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol
            )}
        )
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