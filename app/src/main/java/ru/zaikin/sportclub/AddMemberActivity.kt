package ru.zaikin.sportclub

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import ru.zaikin.sportclub.data.SportclubContract

class AddMemberActivity : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText

    private lateinit var group: EditText
    private lateinit var spinner: Spinner
    private var gender: Int = 0
    private lateinit var spinnerAdapter: ArrayAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_member)

        firstName = findViewById<EditText>(R.id.firstName)
        lastName = findViewById<EditText>(R.id.lastName)
        spinner = findViewById<Spinner>(R.id.spinner)
        group = findViewById<EditText>(R.id.group)

        spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_gender,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGender: String = parent!!.getItemAtPosition(position).toString()

                when (selectedGender) {
                    "Unknown" -> gender = SportclubContract.MemberEntry.GENDER_UNKNOWN
                    "Male" -> gender = SportclubContract.MemberEntry.GENDER_MALE
                    "Female" -> gender = SportclubContract.MemberEntry.GENDER_FEMALE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = 0
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_member_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_member -> {
                insertMember()
                return true
            }
            R.id.delete_member -> return true
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun insertMember() {
        val firstName: String = firstName.text.toString().trim()
        val lastName: String = lastName.text.toString().trim()
        val sport: String = group.text.toString().trim()

        val contentValues: ContentValues = ContentValues()

        contentValues.put(SportclubContract.MemberEntry.COLUMN_FIRST_NAME, firstName)
        contentValues.put(SportclubContract.MemberEntry.COLUMN_LAST_NAME, lastName)
        contentValues.put(SportclubContract.MemberEntry.COLUMN_GROUP, sport)
        contentValues.put(SportclubContract.MemberEntry.COLUMN_GENDER, gender)

        val contentResolver: ContentResolver = contentResolver
        val uri: Uri? = contentResolver.insert(SportclubContract.MemberEntry.BASE_CONTENT_URI, contentValues)

        if (uri == null) {
            Log.d("insertMember method: ", "uri is null")
        }


    }

}