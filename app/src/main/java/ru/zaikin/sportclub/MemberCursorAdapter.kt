package ru.zaikin.sportclub

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import ru.zaikin.sportclub.data.SportclubContract

class MemberCursorAdapter(context: Context, c: Cursor, autoRequery: Boolean) :
    CursorAdapter(context, c, autoRequery) {


    override fun newView(
        context: Context?,
        cursor: Cursor?,
        parent: ViewGroup?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.member_item, parent, false)
    }

    override fun bindView(
        view: View?,
        context: Context?,
        cursor: Cursor?
    ) {
        val firstName: TextView = view!!.findViewById<TextView>(R.id.firstNameTextView)
        val lastName: TextView = view.findViewById<TextView>(R.id.lastNameTextView)
        val group: TextView = view.findViewById<TextView>(R.id.chosenSportTextView)

        val sqlName = cursor!!.getString(cursor.getColumnIndexOrThrow(SportclubContract.MemberEntry.COLUMN_FIRST_NAME))
        val sqlLastName = cursor.getString(cursor.getColumnIndexOrThrow(SportclubContract.MemberEntry.COLUMN_LAST_NAME))
        val sqlGroup = cursor.getString(cursor.getColumnIndexOrThrow(SportclubContract.MemberEntry.COLUMN_GROUP))

        firstName.text = sqlName
        lastName.text = sqlLastName
        group.text = sqlGroup

    }
}