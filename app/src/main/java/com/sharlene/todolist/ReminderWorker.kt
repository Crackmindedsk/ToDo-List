package com.sharlene.todolist

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.reflect.Parameter

class ReminderWorker(val context: Context, val parameter: WorkerParameters):Worker(context,parameter) {
    override fun doWork(): Result {
        Notification(context).createNotification(
            "Hello",
            "Reminder"
        )
        return Result.success()
    }
}