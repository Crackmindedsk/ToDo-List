package com.sharlene.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.Ringtone
import com.sharlene.todolist.model.TaskAdapter
import java.util.*

class TaskDbHelper(context: Context):SQLiteOpenHelper(context, database,null, version) {

    companion object{
        private const val database="Task"
        private const val version = 1
        private const val tone =R.raw.beep_beep_tone
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE = "CREATE TABLE tasks (task_name VARCHAR, initial INTEGER, final INTEGER , date VARCHAR DEFAULT NULL, time VARCHAR DEFAULT NULL, reminder VARCHAR DEFAULT NULL,status INT DEFAULT 0, ringtone INT DEFAULT $tone, delaytime LONG DEFAULT 0 )"
        p0?.execSQL(CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(p0)
    }

    fun insert( taskName: String, intial: Int, final: Int, date: String, time: String, reminder:String, db:SQLiteDatabase?){
        val value: ContentValues = ContentValues().apply {
            put("task_name", taskName)
            put("initial", intial)
            put("final", final)
            put("date",date)
            put("time",time)
            put("reminder",reminder)
        }
        db?.insert("tasks",null,value)
    }

    fun readAll():Int{
        val db:SQLiteDatabase=readableDatabase
        val count = DatabaseUtils.queryNumEntries(db,"tasks")
        return count.toInt()
    }

    fun readComplete():Int{
        val query = "SELECT * FROM tasks WHERE status = 1"
        val db:SQLiteDatabase = readableDatabase
        val cursor:Cursor = db.rawQuery(query,null)
        val num = cursor.count
        val count = DatabaseUtils.queryNumEntries(db,"tasks","status = 1")
        return num
    }

    fun readInComplete():Int{
        val query = "SELECT * FROM tasks WHERE status = 0"
        val db:SQLiteDatabase = readableDatabase
        val cursor:Cursor = db.rawQuery(query,null)
        val num = cursor.count
        return num
    }

    fun readSpecific(Name: String): Cursor? {
        val db =writableDatabase
        val select = "SELECT * FROM tasks WHERE task_name = '$Name'"
        return db.rawQuery(select,null)
    }

    fun update(Name:String,Initial: Int){
        val db = this.writableDatabase
        val select = "UPDATE tasks SET initial = '$Initial' WHERE task_name = '$Name'"
        db.execSQL(select)
    }
    fun updateRemainder(Name: String,reminder:String){
        val db = this.writableDatabase
        val select = "UPDATE tasks SET reminder = '$reminder' WHERE task_name = '$Name'"
        db.execSQL(select)
    }

    fun delete (Name: String){
        val db : SQLiteDatabase = this.writableDatabase
        val delete = "DELETE FROM tasks WHERE task_name = '$Name'"
        db.execSQL(delete)
    }

    fun CompletedStatus(Name: String){
        val db:SQLiteDatabase = this.writableDatabase
        val status = "UPDATE tasks SET status = 1 WHERE task_name = '$Name'"
        db.execSQL(status)
    }

    fun InCompleteStatus(Name: String){
        val db:SQLiteDatabase = this.writableDatabase
        val status = "UPDATE tasks SET status = 0 WHERE task_name = '$Name'"
        db.execSQL(status)
    }
    fun AlterTable(){
        val db:SQLiteDatabase = this.writableDatabase
        val alter= "ALTER TABLE tasks ADD COLUMN ringtone INT DEFAULT 0 "
        db.execSQL(alter)
    }
    fun insertRingtone(Ringtone: Int){
        val db = this.writableDatabase
        val update= " UPDATE tasks SET ringtone = '$Ringtone'"
        db.execSQL(update)
    }
    fun selectRingtone(): Int {
        val db = this.readableDatabase
        val select:Cursor = db.rawQuery("SELECT ringtone FROM tasks LIMIT 1", arrayOf())
        select.moveToFirst()
        var tone:Int
        do {
           tone = select.getInt(0)
        }while (select.moveToNext())
        return tone
    }
    fun insertDelay(taskName: String, timedelay:Long){
        val db = this.writableDatabase
        val update= " UPDATE tasks SET delaytime = '$timedelay' WHERE task_name = '$taskName'"
        db.execSQL(update)
    }
    fun selectDelay(taskName: String): Long {
        val db = this.readableDatabase
        val select:Cursor = db.rawQuery("SELECT delaytime FROM tasks WHERE task_name = '$taskName'", arrayOf())
        select.moveToFirst()
        var tone:Long
        do {
            tone = select.getLong(0)
        }while (select.moveToNext())
        return tone
    }

    fun editdata( task:String, taskName: String, intial: Int, final: Int, date: String,time: String){
        val db = this. writableDatabase
        val update = "UPDATE tasks SET task_name='$taskName', initial='$intial', final='$final', date = '$date', time = '$time' WHERE task_name='$task' "
        db.execSQL(update)
    }


}