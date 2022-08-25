package com.sharlene.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView

class AlarmToneActivity : AppCompatActivity() {

    var selected_tone: Int=0
    var dbHelper: TaskDbHelper? = null
    lateinit var tone1:CardView
    lateinit var tone2:CardView
    lateinit var tone3:CardView
    lateinit var tone4:CardView
    lateinit var tone5:CardView
    lateinit var tone6:CardView
    lateinit var tone7:CardView
    lateinit var tone8:CardView
    lateinit var tone9:CardView
    lateinit var tone10:CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_tone)
        tone1= findViewById(R.id.tone1)
        tone2= findViewById(R.id.tone2)
        tone3= findViewById(R.id.tone3)
        tone4= findViewById(R.id.tone4)
        tone5= findViewById(R.id.tone5)
        tone6= findViewById(R.id.tone6)
        tone7= findViewById(R.id.tone7)
        tone8= findViewById(R.id.tone8)
        tone9= findViewById(R.id.tone9)
        tone10 = findViewById(R.id.tone10)
        dbHelper = TaskDbHelper(this)

        tone1.setOnClickListener{v ->
            selected_tone = R.raw.beep_beep_tone
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))
        }
        tone2.setOnClickListener { v ->
            selected_tone = R.raw.calling_santa
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone3.setOnClickListener { v ->
            selected_tone = R.raw.clock_alarm
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone4.setOnClickListener { v ->
            selected_tone = R.raw.do_mi_so
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone5.setOnClickListener { v ->
            selected_tone = R.raw.futurist_sirens
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone6.setOnClickListener { v ->
            selected_tone = R.raw.high_alert_sound
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone7.setOnClickListener { v ->
            selected_tone = R.raw.jungle_tone
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone8.setOnClickListener { v ->
            selected_tone = R.raw.nuclear_alarm
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone9.setOnClickListener { v ->
            selected_tone = R.raw.top_touches_wow
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}
        tone10.setOnClickListener { v ->
            selected_tone = R.raw.vibrating_sound
            dbHelper!!.insertRingtone(selected_tone)
            startActivity(Intent( applicationContext,MainActivity::class.java))}

    }

}