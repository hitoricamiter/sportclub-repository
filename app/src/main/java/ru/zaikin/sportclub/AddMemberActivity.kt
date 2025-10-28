package ru.zaikin.sportclub

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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
            R.id.save_member -> return true
            R.id.delete_member -> return true
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}