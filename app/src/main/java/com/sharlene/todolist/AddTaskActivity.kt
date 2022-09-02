package com.sharlene.todolist

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sharlene.todolist.alarm.MyAlarm
import java.util.*
import java.util.concurrent.TimeUnit

class AddTaskActivity : AppCompatActivity() {

    lateinit var name: TextInputEditText
    lateinit var initial: TextInputEditText
    lateinit var final: TextInputEditText
    lateinit var date: TextInputEditText
    lateinit var time: TextInputEditText
    val selectAlarm=AlarmToneActivity()
    var cal = Calendar.getInstance()
    var dbHelper: TaskDbHelper? = null
    var chosenyear=0
    var chosenmonth=0
    var chosenday=0
    var chosenhour=0
    var chosenmin=0
    var delayInSeconds :Long = 0

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


        btDismiss.setOnClickListener {
            if (name.text.toString() == "" || initial.text.toString() == "" || final.text.toString() == "" || date.text.toString()==""  || time.text.toString()=="") {
                Toast.makeText(this, "Fill the required details", Toast.LENGTH_SHORT).show()
            } else {
                storeData()
                setReminder()
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
                val str = String.format("%d / %d / %d",tday,tmonth+1,tyear)
                date.text = str.toEditable()
                chosenyear = tyear
                chosenmonth= tmonth
                chosenday=tday
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun storeData() {
        val db: SQLiteDatabase = dbHelper!!.writableDatabase
        val nameTask = name.text.toString()
        val initialTask = initial.text.toString().toInt()
        val finalTask = final.text.toString().toInt()
        val date = date.text.toString()
        val time = time.text.toString()
        val reminder = ""
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
                chosenhour=hour
                chosenmin=minute
            },hour,min,false)
        timePicker.show()
    }
    fun setReminder(){
        val userSelectedDateTime = Calendar.getInstance()
        userSelectedDateTime.set(chosenyear,chosenmonth,chosenday,chosenhour,chosenmin)
        val todayDateTime = Calendar.getInstance()
        delayInSeconds = ((userSelectedDateTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L))


        val str = dbHelper!!.selectRingtone()
        Toast.makeText(applicationContext,"$str",Toast.LENGTH_SHORT).show()
        createWorkRequest(str,delayInSeconds,name.text.toString())
        Toast.makeText(this,"Reminder set",Toast.LENGTH_SHORT).show()
//        val intent = Intent(applicationContext,MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,0)
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.set(AlarmManager.RTC_WAKEUP,delayInSeconds,pendingIntent)

    }

    fun createWorkRequest(message: Int, timeDelayInSeconds: Long,Title :String){
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                "title" to Title,
                "message" to "Your have pending task $Title",
                    "tone" to message
            )
            )
            .build()
        WorkManager.getInstance(applicationContext).enqueue(myWorkRequest)
    }

}