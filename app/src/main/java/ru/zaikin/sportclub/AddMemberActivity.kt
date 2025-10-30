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

        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        spinner = findViewById(R.id.spinner)
        group = findViewById(R.id.group)

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
                val selectedGender = parent!!.getItemAtPosition(position).toString()
                gender = when (selectedGender) {
                    "Unknown" -> SportclubContract.MemberEntry.GENDER_UNKNOWN
                    "Male" -> SportclubContract.MemberEntry.GENDER_MALE
                    "Female" -> SportclubContract.MemberEntry.GENDER_FEMALE
                    else -> 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = SportclubContract.MemberEntry.GENDER_UNKNOWN
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

    private fun insertMember() {
        val firstNameStr = firstName.text.toString().trim()
        val lastNameStr = lastName.text.toString().trim()
        val sport = group.text.toString().trim()

        val values = ContentValues().apply {
            put(SportclubContract.MemberEntry.COLUMN_FIRST_NAME, firstNameStr)
            put(SportclubContract.MemberEntry.COLUMN_LAST_NAME, lastNameStr)
            put(SportclubContract.MemberEntry.COLUMN_GROUP, sport)
            put(SportclubContract.MemberEntry.COLUMN_GENDER, gender)
        }

        val resolver: ContentResolver = contentResolver


        val uri: Uri? = resolver.insert(SportclubContract.MemberEntry.CONTENT_URI, values)

        if (uri == null) {
            Toast.makeText(this, "Ошибка при добавлении!", Toast.LENGTH_SHORT).show()
            Log.d("insertMember", "URI is null")
        } else {
            Toast.makeText(this, "Участник добавлен!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
