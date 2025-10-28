package ru.zaikin.sportclub.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(
    context: Context?
) : SQLiteOpenHelper(
    context,
    SportclubContract.MemberEntry.DATABASE_NAME,
    null,
    SportclubContract.MemberEntry.DATABASE_VERSION
) {


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CLUB_TABLE: String =
            "CREATE TABLE ${SportclubContract.MemberEntry.TABLE_NAME} (${SportclubContract.MemberEntry.COLUMN_ID} INTEGER PRIMARY KEY," +
                    " ${SportclubContract.MemberEntry.COLUMN_FIRST_NAME} TEXT," +
                    " ${SportclubContract.MemberEntry.COLUMN_LAST_NAME} TEXT)" +
                    " ${SportclubContract.MemberEntry.COLUMN_GENDER} INTEGER NOT NULL" +
                    " ${SportclubContract.MemberEntry.COLUMN_GROUP} TEXT"

        db?.execSQL(CREATE_CLUB_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS ${SportclubContract.MemberEntry.DATABASE_NAME}")
        onCreate(db)
    }
}