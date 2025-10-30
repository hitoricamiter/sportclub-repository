package ru.zaikin.sportclub.data

import android.net.Uri
import android.provider.BaseColumns
import androidx.core.net.toUri

class SportclubContract {

    class MemberEntry : BaseColumns {
        companion object {
            val SCHEME: String = "content://"
            val AUTHORITY: String = "ru.zaikin.sportclub"

            val BASE_CONTENT_URI: Uri = (SCHEME + AUTHORITY).toUri()

            val DATABASE_VERSION: Int = 1
            val DATABASE_NAME: String = "sport_club"
            val TABLE_NAME: String = "members"
            val COLUMN_ID: String = BaseColumns._ID
            val COLUMN_FIRST_NAME: String = "first_name"
            val COLUMN_LAST_NAME: String = "last_name"
            val COLUMN_GENDER: String = "gender"
            val COLUMN_GROUP: String = "sport"

            val GENDER_UNKNOWN: Int = 0
            val GENDER_MALE: Int = 1
            val GENDER_FEMALE: Int = 2

            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "members")

        }
    }
}