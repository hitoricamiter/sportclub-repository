package ru.zaikin.sportclub

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
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
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import ru.zaikin.sportclub.data.SportclubContract
import ru.zaikin.sportclub.data.SportclubContract.MemberEntry

class AddMemberActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var group: EditText
    private lateinit var spinner: Spinner
    private var currentMemberUri: Uri? = null

    private var gender: Int = 0
    private lateinit var spinnerAdapter: ArrayAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_member)

        // Получаем URI из интента и присваиваем свойству класса
        currentMemberUri = intent.data

        if (currentMemberUri == null) {
            setTitle("Add a member")
        } else {
            setTitle("Edit the member")
            supportLoaderManager.initLoader(321, null, this)
        }

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
                if (currentMemberUri == null) {
                    saveMember()
                } else {
                    updateMember()
                }
                return true
            }

            R.id.delete_member -> {
                if (currentMemberUri != null) {
                    deleteMember()
                }
                return true
            }

            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMember() {
        val firstNameStr = firstName.text.toString().trim()
        val lastNameStr = lastName.text.toString().trim()
        val sport = group.text.toString().trim()

        if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || sport.isEmpty()) {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentMemberUri == null) {
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
        } else {
            val values = ContentValues().apply {
                put(SportclubContract.MemberEntry.COLUMN_FIRST_NAME, firstNameStr)
                put(SportclubContract.MemberEntry.COLUMN_LAST_NAME, lastNameStr)
                put(SportclubContract.MemberEntry.COLUMN_GROUP, sport)
                put(SportclubContract.MemberEntry.COLUMN_GENDER, gender)
            }

            val rowsUpdated = contentResolver.update(currentMemberUri!!, values, null, null)

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Участник обновлен!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Ошибка при обновлении!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMember() {
        val firstNameStr = firstName.text.toString().trim()
        val lastNameStr = lastName.text.toString().trim()
        val sport = group.text.toString().trim()

        if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || sport.isEmpty()) {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put(SportclubContract.MemberEntry.COLUMN_FIRST_NAME, firstNameStr)
            put(SportclubContract.MemberEntry.COLUMN_LAST_NAME, lastNameStr)
            put(SportclubContract.MemberEntry.COLUMN_GROUP, sport)
            put(SportclubContract.MemberEntry.COLUMN_GENDER, gender)
        }

        val rowsUpdated = contentResolver.update(currentMemberUri!!, values, null, null)

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Участник обновлен!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Ошибка при обновлении!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteMember() {
        // Реализуйте удаление участника
        val rowsDeleted = contentResolver.delete(currentMemberUri!!, null, null)

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Участник удален!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Ошибка при удалении!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Cursor?> {
        val projection = arrayOf(
            SportclubContract.MemberEntry.COLUMN_ID,
            SportclubContract.MemberEntry.COLUMN_FIRST_NAME,
            SportclubContract.MemberEntry.COLUMN_LAST_NAME,
            SportclubContract.MemberEntry.COLUMN_GENDER,
            SportclubContract.MemberEntry.COLUMN_GROUP
        )

        return CursorLoader(this, currentMemberUri!!, projection, null, null, null)
    }

    override fun onLoadFinished(
        loader: Loader<Cursor?>,
        data: Cursor?
    ) {
        if (data != null && data.moveToFirst()) {
            val firstNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME)
            val lastNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_LAST_NAME)
            val genderColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_GENDER)
            val groupNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_GROUP)

            val firstNameEdit =
                if (firstNameColumnIndex >= 0) data.getString(firstNameColumnIndex) else ""
            val lastNameEdit =
                if (lastNameColumnIndex >= 0) data.getString(lastNameColumnIndex) else ""
            val genderEdit =
                if (genderColumnIndex >= 0) data.getInt(genderColumnIndex) else MemberEntry.GENDER_UNKNOWN
            val groupEdit =
                if (groupNameColumnIndex >= 0) data.getString(groupNameColumnIndex) else ""

            firstName.setText(firstNameEdit)
            lastName.setText(lastNameEdit)
            group.setText(groupEdit)

            when (genderEdit) {
                MemberEntry.GENDER_MALE -> spinner.setSelection(1)
                MemberEntry.GENDER_FEMALE -> spinner.setSelection(2)
                else -> spinner.setSelection(0)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        firstName.setText("")
        lastName.setText("")
        group.setText("")
        spinner.setSelection(0)
    }
}