package ru.zaikin.sportclub.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlin.Int

class SportclubContentProvider : ContentProvider() {

    private lateinit var db: DatabaseHandler
    private val CODE_MEMBERS = 1
    private val CODE_MEMBER_BY_ID = 2

    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("ru.zaikin.sportclub", "members", CODE_MEMBERS)
        addURI("ru.zaikin.sportclub", "members/#", CODE_MEMBER_BY_ID)
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {

        val sqldb : SQLiteDatabase = db.writableDatabase
        lateinit var cursor: Cursor
        val match: Int = uriMatcher.match(uri)

        when (match) {
            CODE_MEMBERS -> {
                val id : Long = sqldb.insert("${SportclubContract.MemberEntry.TABLE_NAME}", null, values)

                if (id == -1L) {
                    Log.d("insert method:", "Insertion of data wasn't completed successfully $id and uri: $uri")
                    return null
                }
                return ContentUris.withAppendedId(uri, id)
            }

            else -> Toast.makeText(context, "Code error", Toast.LENGTH_LONG)
        }

        return null
    }

    override fun onCreate(): Boolean {
        db = DatabaseHandler(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {

        val sqldb: SQLiteDatabase = db.readableDatabase
        lateinit var cursor: Cursor
        val match: Int = uriMatcher.match(uri)

        when (match) {
            CODE_MEMBERS -> cursor = sqldb.query(
                "${SportclubContract.MemberEntry.TABLE_NAME}",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            CODE_MEMBER_BY_ID -> {
                val newSelection: String = SportclubContract.MemberEntry.COLUMN_ID + "=?"
                val newSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                cursor = sqldb.query(
                    "${SportclubContract.MemberEntry.TABLE_NAME}",
                    projection,
                    newSelection,
                    newSelectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }

            else -> Toast.makeText(context, "Code error", Toast.LENGTH_LONG)
        }

        return cursor
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}