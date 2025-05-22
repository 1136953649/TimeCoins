package com.example.timecoins

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TimeCoins.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CELLS = "cells"
        private const val COLUMN_ID = "id"
        private const val COLUMN_POSITION = "position"
        private const val COLUMN_COLOR_INDEX = "color_index"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CELLS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_POSITION INTEGER NOT NULL,
                $COLUMN_COLOR_INDEX INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CELLS")
        onCreate(db)
    }

    fun saveCellColor(date: String, position: Int, color_index: Int) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_CELLS WHERE $COLUMN_POSITION = ? AND $COLUMN_DATE = ?", arrayOf(position,date))
        db.execSQL("INSERT INTO $TABLE_CELLS ($COLUMN_DATE, $COLUMN_POSITION, $COLUMN_COLOR_INDEX) VALUES (?, ?, ?)", 
            arrayOf(date, position, color_index))
        db.close()
    }

    fun getCellColor(date: String, position: Int): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_COLOR_INDEX FROM $TABLE_CELLS WHERE $COLUMN_POSITION = ?  AND $COLUMN_DATE = ?", 
            arrayOf(position.toString(), date)
        )
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }

    fun getAllCellColorsByDate(date: String): Map<Int, Int> {
        val colors = mutableMapOf<Int, Int>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_POSITION, $COLUMN_COLOR_INDEX FROM $TABLE_CELLS WHERE $COLUMN_DATE = ?", arrayOf(date))
        while (cursor.moveToNext()) {
            colors[cursor.getInt(0)] = cursor.getInt(1)
        }
        cursor.close()
        db.close()
        return colors
    }
}