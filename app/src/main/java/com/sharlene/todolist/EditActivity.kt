package com.sharlene.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class EditActivity : AppCompatActivity() {

    lateinit var taskname:TextInputEditText
    lateinit var initial:TextInputEditText
    lateinit var final:TextInputEditText
    lateinit var date:TextInputEditText
    lateinit var timeedit:TextInputEditText
    lateinit var save:Button
    var dbHelper: TaskDbHelper? = null
    var cal = Calendar.getInstance()
    var chosenyear=0
    var chosenmonth=0
    var chosenday=0
    var chosenhour=0
    var chosenmin=0
    var task:String?="Not Exist"

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        dbHelper = TaskDbHelper(this)
        taskname=findViewById(R.id.change1)
        initial=findViewById(R.id.change2)
        final=findViewById(R.id.change3)
        date=findViewById(R.id.change4)
        timeedit=findViewById(R.id.change5)
        save=findViewById(R.id.change6)

        val extras:Bundle? = intent.extras

        if(extras != null){
            task = extras.getString("name")
            val i = extras.getString("initial")
            val f = extras.getString("final")
            val d = extras.getString("date")
            val t = extras.getString("time")

            taskname.text= task?.toEditable()
            initial.text= i?.toEditable()
            final.text= f?.toEditable()
            date.text= d?.toEditable()
            timeedit.text= t?.toEditable()
        }
        date.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, tyear, tmonth, tday ->
                    val str = String.format("%d / %d / %d",tday,tmonth+1,tyear)
                    date.text = str.toEditable()
                    chosenyear = tyear
                    chosenmonth= tmonth
                    chosenday=tday
                }, year, month, day)

            datePickerDialog.show()
        }
        timeedit.setOnClickListener {
            val timePicker: TimePickerDialog
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val min = currentTime.get(Calendar.MINUTE)

            timePicker =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val str = String.format("%d : %d",hour,minute)
                    timeedit.text = str.toEditable()
                    chosenhour=hour
                    chosenmin=minute
                },hour,min,false)
            timePicker.show()
        }
        save.setOnClickListener {
            dbHelper!!.editdata(task.toString(),taskname.text.toString(), initial.text.toString().toInt(), final.text.toString().toInt(), date.text.toString(), timeedit.text.toString())
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}