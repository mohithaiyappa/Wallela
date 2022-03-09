package tk.mohithaiyappa.wallela

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,LOW VARCHAR(50),MID VARCHAR(50),HIGH VARCHAR(50))")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(lowResUrl: String?, midResUrl: String?, hiResUrl: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_2, lowResUrl)
        contentValues.put(COL_3, midResUrl)
        contentValues.put(COL_4, hiResUrl)
        return if (db.insert(
                TABLE_NAME,
                null,
                contentValues
            ) == -1L
        ) false else true
    }

    val data: Cursor?
        get() {
            val db = this.writableDatabase
            val i =
                DatabaseUtils.queryNumEntries(db, TABLE_NAME)
                    .toInt()
            return if (i == 0) {
                null
            } else db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }

    fun dataExits(lowResUrl: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        while (cursor.moveToNext()) {
            if (cursor.getString(1) == lowResUrl) return true
        }
        return false
    }

    fun dropEntry(lowResUrl: String): Boolean {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM favorites_table WHERE LOW='$lowResUrl'")
        return true
    }

    companion object {
        private const val DATABASE_NAME = "Favorites.db"
        private const val TABLE_NAME = "favorites_table"
        private const val COL_1 = "ID"
        private const val COL_2 = "LOW"
        private const val COL_3 = "MID"
        private const val COL_4 = "HIGH"
    }
}