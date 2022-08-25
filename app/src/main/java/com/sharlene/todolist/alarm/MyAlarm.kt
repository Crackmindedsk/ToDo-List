package com.sharlene.todolist.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.sharlene.todolist.R

class MyAlarm : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var mp: MediaPlayer = MediaPlayer.create(p0, R.raw.nuclear_alarm)
        mp.start()
    }
}