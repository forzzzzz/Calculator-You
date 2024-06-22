package com.forz.calculator.history

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.Instant
import java.time.ZoneId

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"


        private const val TABLE_NAME = "historyData"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EXPRESSION = "expression"
        private const val COLUMN_RESULT = "result"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_EXPRESSION TEXT, $COLUMN_RESULT TEXT, $COLUMN_DATE INTEGER)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(expression: String, result: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EXPRESSION, expression)
            put(COLUMN_RESULT, result)
            put(COLUMN_DATE, System.currentTimeMillis())
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

   fun selectAllOrderByDate(): MutableList<HistoryData> {
        val historyDataList = mutableListOf<HistoryData>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC")

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val expressionIndex = it.getColumnIndexOrThrow(COLUMN_EXPRESSION)
            val resultIndex = it.getColumnIndexOrThrow(COLUMN_RESULT)
            val dateIndex = it.getColumnIndexOrThrow(COLUMN_DATE)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val expression = it.getString(expressionIndex)
                val result = it.getString(resultIndex)
                val date = it.getLong(dateIndex)
                val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                val historyData = HistoryData(id, expression, result, localDate)
                historyDataList.add(historyData)
            }
        }

        return historyDataList
    }

    fun deleteById(id: Int) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }

    fun modifyAllRecords(modifyExpression: (String) -> String, modifyResult: (String) -> String) {
        val db = writableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val expressionIndex = it.getColumnIndexOrThrow(COLUMN_EXPRESSION)
            val resultIndex = it.getColumnIndexOrThrow(COLUMN_RESULT)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val expression = it.getString(expressionIndex)
                val result = it.getString(resultIndex)

                val newExpression = modifyExpression(expression)
                val newResult = modifyResult(result)

                val contentValues = ContentValues().apply {
                    put(COLUMN_EXPRESSION, newExpression)
                    put(COLUMN_RESULT, newResult)
                }

                db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
            }
        }

        db.close()
    }
}