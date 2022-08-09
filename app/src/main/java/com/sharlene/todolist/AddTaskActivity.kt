package com.sharlene.todolist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    lateinit var name: TextInputEditText
    lateinit var initial: TextInputEditText
    lateinit var final: TextInputEditText
    lateinit var textField: TextInputLayout
    lateinit var date: TextInputEditText
    lateinit var time: TextInputEditText
    var cal = Calendar.getInstance()
    var dbHelper: TaskDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        dbHelper = TaskDbHelper(this)

        val btDismiss = findViewById<Button>(R.id.add_btn)
        name = findViewById(R.id.edit1)
        initial = findViewById(R.id.edit2)
        final = findViewById(R.id.edit3)
        date = findViewById(R.id.date_picker_actions)
        time = findViewById(R.id.time)

        date.setOnClickListener {
            updateDateInView()
        }
        time.setOnClickListener{
            timePickerFun()
        }

        textField = findViewById(R.id.remainder)
        val items = listOf("5 Minute before", "30 Minute before", "1 Hour Before", "Custom Time")
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        btDismiss.setOnClickListener {
            if (name.text.toString() == "" || initial.text.toString() == "" || final.text.toString() == "") {
                Toast.makeText(this, "Fill the required details", Toast.LENGTH_SHORT).show()
            } else {
                storeData()
                Toast.makeText(this, "Data added Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun updateDateInView() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, tyear, tmonth, tday ->
                val str = "$tday/$tmonth/$tyear"
                date.text = str.toEditable()
            }, day, month, year)
        datePickerDialog.show()
    }

    private fun storeData() {
        val db: SQLiteDatabase = dbHelper!!.writableDatabase
        val nameTask = name.text.toString()
        val initialTask = initial.text.toString().toInt()
        val finalTask = final.text.toString().toInt()
        val date = date.text.toString()
        val time = time.text.toString()
        val reminder = textField.editText?.text.toString()
        dbHelper!!.insert(nameTask, initialTask, finalTask,date,time,reminder,db)

    }

    private fun timePickerFun() {
        val timePicker: TimePickerDialog
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val min = currentTime.get(Calendar.MINUTE)

        timePicker =
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                val str = String.format("%d : %d",hour,minute)
                time.text = str.toEditable()
            },hour,min,false)
        timePicker.show()
    }

}