package ru.zaikin.sportclub

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.zaikin.sportclub.data.SportclubContract.MemberEntry

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val MEMBER_LOADER = 123
    private lateinit var memberCursorAdapter: MemberCursorAdapter

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

        memberCursorAdapter = MemberCursorAdapter(this, null, false)
        dataListView.adapter = memberCursorAdapter

        dataListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val intent: Intent = Intent(this@MainActivity, AddMemberActivity::class.java)
                val currentMemberUri: Uri = ContentUris.withAppendedId(MemberEntry.CONTENT_URI, id)
                intent.setData(currentMemberUri)
                startActivity(intent)
            }
        })


        supportLoaderManager.initLoader(MEMBER_LOADER, null, this)

    }

    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Cursor?> {
        val projection = arrayOf(
            MemberEntry.COLUMN_ID,
            MemberEntry.COLUMN_FIRST_NAME,
            MemberEntry.COLUMN_LAST_NAME,
            MemberEntry.COLUMN_GROUP
        )

        val cursorLoader: CursorLoader =
            CursorLoader(this, MemberEntry.CONTENT_URI, projection, null, null, null)

        return cursorLoader
    }

    override fun onLoadFinished(
        loader: Loader<Cursor?>,
        data: Cursor?
    ) {
        memberCursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        memberCursorAdapter.swapCursor(null)
    }

}