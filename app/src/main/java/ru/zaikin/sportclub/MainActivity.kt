package ru.zaikin.sportclub

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.zaikin.sportclub.data.SportclubContract.MemberEntry

class MainActivity : AppCompatActivity() {

    private lateinit var dataListView: ListView

    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dataListView = findViewById<ListView>(R.id.dataListView)

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

        val memberCursorAdapter = MemberCursorAdapter(this, cursor, false)
        dataListView.adapter = memberCursorAdapter

    }

}