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

class SportclubContentProvider : ContentProvider() {

    private lateinit var db: DatabaseHandler

    companion object {
        private const val CODE_MEMBERS = 1
        private const val CODE_MEMBER_BY_ID = 2

        private const val AUTHORITY = "ru.zaikin.sportclub"
        private const val PATH_MEMBERS = "members"
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH_MEMBERS, CODE_MEMBERS)
        addURI(AUTHORITY, "$PATH_MEMBERS/#", CODE_MEMBER_BY_ID)
    }

    override fun onCreate(): Boolean {
        db = DatabaseHandler(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val sqldb: SQLiteDatabase = db.writableDatabase
        return when (uriMatcher.match(uri)) {

            CODE_MEMBERS -> {
                val id = sqldb.insert(SportclubContract.MemberEntry.TABLE_NAME, null, values)

                if (id == -1L) {
                    Log.d("insert", "Insert failed: uri=$uri")
                    null
                } else {
                    ContentUris.withAppendedId(uri, id)
                }
            }

            else -> {
                Toast.makeText(context, "Invalid URI for insert", Toast.LENGTH_SHORT).show()
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {

        val sqldb = db.readableDatabase

        val cursor = when (uriMatcher.match(uri)) {
            CODE_MEMBERS -> sqldb.query(
                SportclubContract.MemberEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            CODE_MEMBER_BY_ID -> {
                val newSelection = "${SportclubContract.MemberEntry.COLUMN_ID}=?"
                val newArgs = arrayOf(ContentUris.parseId(uri).toString())

                sqldb.query(
                    SportclubContract.MemberEntry.TABLE_NAME,
                    projection,
                    newSelection,
                    newArgs,
                    null,
                    null,
                    sortOrder
                )
            }

            else -> {
                Toast.makeText(context, "Invalid URI query", Toast.LENGTH_SHORT).show()
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }

        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int {
        Toast.makeText(context, "Delete not implemented", Toast.LENGTH_SHORT).show()
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String?>?): Int {
        Toast.makeText(context, "Update not implemented", Toast.LENGTH_SHORT).show()
        return 0
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CODE_MEMBERS -> "vnd.android.cursor.dir/$AUTHORITY.$PATH_MEMBERS"
            CODE_MEMBER_BY_ID -> "vnd.android.cursor.item/$AUTHORITY.$PATH_MEMBERS"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
