package ru.zaikin.sportclub

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.zaikin.sportclub.data.SportclubContract.MemberEntry

class MainActivity : AppCompatActivity() {

    private lateinit var dataTextView: TextView

    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dataTextView = findViewById<TextView>(R.id.dataTextView)

        floatingButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        floatingButton.setOnClickListener {
            val intent: Intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        displayData()
    }

    private fun displayData() {
        val projection = arrayOf(
            MemberEntry.COLUMN_ID,
            MemberEntry.COLUMN_FIRST_NAME,
            MemberEntry.COLUMN_LAST_NAME,
            MemberEntry.COLUMN_GENDER,
            MemberEntry.COLUMN_GROUP
        )

        val cursor = contentResolver.query(MemberEntry.CONTENT_URI, projection, null, null, null)
            ?: return

        dataTextView.text = "All members:\n\n"
        dataTextView.append("ID | First | Last | Gender | Group\n")

        val idCol = cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_ID)
        val firstCol = cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_FIRST_NAME)
        val lastCol = cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_LAST_NAME)
        val genderCol = cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_GENDER)
        val groupCol = cursor.getColumnIndexOrThrow(MemberEntry.COLUMN_GROUP)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(idCol)
            val first = cursor.getString(firstCol)
            val last = cursor.getString(lastCol)
            val gender = cursor.getInt(genderCol)
            val group = cursor.getString(groupCol)

            dataTextView.append("$id | $first | $last | $gender | $group\n")
        }

        cursor.close()
    }

}