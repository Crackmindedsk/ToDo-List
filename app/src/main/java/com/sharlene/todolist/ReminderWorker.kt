package com.sharlene.todolist

import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.SystemClock.sleep
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(val context: Context, val parameter: WorkerParameters):Worker(context,parameter) {
    override fun doWork(): Result {
        val title =inputData.getString("title")
        val message = inputData.getString("message")
        Notification(context).createNotification(
            title.toString(),
            message.toString()
        )
        val tone = inputData.getInt("tone",R.raw.beep_beep_tone)
        playRingtone(tone)
        return Result.success()
    }
    fun playRingtone(ring:Int) {
    val ringtone by lazy {
        RingtoneManager.getRingtone(
            context,
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" +ring)
        )
    }
        ringtone.play()
        sleep(8000)
//        delay(8000)
        ringtone.stop()

    }
}