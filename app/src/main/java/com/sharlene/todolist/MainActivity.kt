package com.sharlene.todolist

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import android.os.Bundle
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlin.collections.ArrayList
import com.sharlene.todolist.TaskDbHelper
import com.sharlene.todolist.model.CompleteTaskAdapter
import com.sharlene.todolist.model.TaskAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.jar.Attributes

class MainActivity : AppCompatActivity() {

    private var progressBarStatus = 0

    var dbHelper: TaskDbHelper? = null
    lateinit var TaskName: ArrayList<String>
    var initialValue: ArrayList<Int>? = null
    var finalValue: ArrayList<Int>? = null
    var dateValue: ArrayList<String>? = null
    var timeValue: ArrayList<String>? = null
    var reminderValue: ArrayList<String>? = null
    lateinit var statusValue:ArrayList<Int>
    var adapter: TaskAdapter? = null
    private var listener: TaskAdapter.RecyclerViewClickListener? = null
    private var listenerComplete : CompleteTaskAdapter.RecyclerViewClickListener? = null
    lateinit var bottomNav: BottomNavigationView
    lateinit var bottomNav2: BottomNavigationView
    lateinit var status:CheckBox
    lateinit var TaskNameComplete: ArrayList<String>
    var initialValueComplete: ArrayList<Int>? = null
    var finalValueComplete: ArrayList<Int>? = null
    var dateValueComplete: ArrayList<String>? = null
    var timeValueComplete: ArrayList<String>? = null
    var reminderValueComplete: ArrayList<String>? = null
    lateinit var statusValueComplete:ArrayList<Int>
    var adapterComplete: TaskAdapter? = null



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting) {
            return true
        } else if (id == R.id.alarm_tone) {
            startActivity(Intent(applicationContext,AlarmToneActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view)
        val viewList = findViewById<View>(R.id.recycler_view) as RecyclerView
        val completeList = findViewById<View>(R.id.recycler_view_completed) as RecyclerView
        TaskName = ArrayList()
        initialValue = ArrayList()
        finalValue = ArrayList()
        dateValue = ArrayList()
        timeValue = ArrayList()
        reminderValue = ArrayList()
        statusValue = ArrayList()
        TaskNameComplete = ArrayList()
        initialValueComplete = ArrayList()
        finalValueComplete = ArrayList()
        dateValueComplete = ArrayList()
        timeValueComplete = ArrayList()
        reminderValueComplete = ArrayList()
        statusValueComplete = ArrayList()


        dbHelper = TaskDbHelper(this)
        val empty: ConstraintLayout = findViewById(R.id.empty_view)

//            selectData()
//            setOnClickListener()
//        dbHelper!!.updateCheck()
//        dbHelper!!.AlterTable()

        if (dbHelper!!.readAll() == 0) {
            viewList.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
            if(dbHelper!!.readInComplete() == 0){
                viewList.visibility = View.GONE
            }else{
                viewList.visibility = View.VISIBLE

                selectData()
                setOnClickListener()
                adapter = TaskAdapter(
                    this,
                    TaskName!!,
                    initialValue!!,
                    finalValue!!,
                    dateValue!!,
                    timeValue!!,
                    reminderValue!!,
                    statusValue,
                    listener
                )
                viewList.adapter = adapter
                viewList.layoutManager = LinearLayoutManager(this)
            }
            completeList.visibility=View.GONE

//            if(dbHelper!!.readComplete() == 0){
//                completeList.visibility = View.GONE
//            }else {
//                completeList.visibility = View.VISIBLE
//                selectCompleteData()
//                setOnClickListener()
//                adapterComplete = TaskAdapter(
//                    this,
//                    TaskNameComplete,
//                    initialValueComplete!!,
//                    finalValueComplete!!,
//                    dateValueComplete!!,
//                    timeValueComplete!!,
//                    reminderValueComplete!!,
//                    statusValueComplete,
//                    listener
//                )
//                completeList.adapter = adapterComplete
//                completeList.layoutManager = LinearLayoutManager(this)
//            }

        }



        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { v ->
            val intent = Intent(applicationContext, AddTaskActivity::class.java)
            startActivity(intent)
        }

        //Bottom Navigation
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    return@setOnNavigationItemReselectedListener
                }
                R.id.graph -> {
                    return@setOnNavigationItemReselectedListener
                }
                R.id.goal -> {
                    return@setOnNavigationItemReselectedListener
                }
                R.id.general_task -> {
                    return@setOnNavigationItemReselectedListener
                }
            }
        }

    }







//    private  fun loadFragment(fragment: Fragment){
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container,fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }


    private fun selectData() {
        val db: SQLiteDatabase = dbHelper!!.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", arrayOf())
        cursor.moveToFirst()
        do {
            val taskn: String = cursor.getString(0)
            val taski: Int = cursor.getInt(1)
            val taskf: Int = cursor.getInt(2)
            val taskd: String = cursor.getString(3)
            val taskt: String = cursor.getString(4)
            val taskr: String = cursor.getString(5)
            val tasks: Int = cursor.getInt(6)
            TaskName!!.add(taskn)
            initialValue!!.add(taski)
            finalValue!!.add(taskf)
            dateValue?.add(taskd)
            timeValue?.add(taskt)
            reminderValue?.add(taskr)
            statusValue.add(tasks)
        } while (cursor.moveToNext())
    }
    private fun selectCompleteData() {
        val db: SQLiteDatabase = dbHelper!!.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE status = 1", arrayOf())
        cursor.moveToFirst()
        do {
            val taskn: String = cursor.getString(0)
            val taski: Int = cursor.getInt(1)
            val taskf: Int = cursor.getInt(2)
            val taskd: String = cursor.getString(3)
            val taskt: String = cursor.getString(4)
            val taskr: String = cursor.getString(5)
            val tasks: Int = cursor.getInt(6)
            TaskNameComplete.add(taskn)
            initialValueComplete!!.add(taski)
            finalValueComplete!!.add(taskf)
            dateValueComplete?.add(taskd)
            timeValueComplete?.add(taskt)
            reminderValueComplete?.add(taskr)
            statusValueComplete.add(tasks)
        } while (cursor.moveToNext())
    }

    private fun selectSingle(string: String) {
        val db: SQLiteDatabase = dbHelper!!.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE task_name = '$string'", arrayOf())
        cursor.moveToFirst()
        do {
            val taskn: String = cursor.getString(0)
            val taski: Int = cursor.getInt(1)
            val taskf: Int = cursor.getInt(2)
            TaskName!!.add(taskn)
            initialValue!!.add(taski)
            finalValue!!.add(taskf)
        } while (cursor.moveToNext())
    }

    private fun Count(): Int {
        val db: SQLiteDatabase = dbHelper!!.readableDatabase
        val rowCount = db.rawQuery("SELECT COUNT(*) FROM tasks", arrayOf())
        return rowCount.toString().toInt()
    }


//    private fun setOnCompleteClick(){
//
//
////        listenerComplete = object : CompleteTaskAdapter.RecyclerViewClickListener {
////            override fun onClick(view: View?, position: Int) {
////                status = findViewById<CheckBox>(R.id.statusCheck)
////                status.setOnCheckedChangeListener { compoundButton,   b ->
////
////
////            }
////        }
//    }

    private fun setOnClickListener() {

        listener = object : TaskAdapter.RecyclerViewClickListener {

            override fun onClick(view: View?, position: Int) {

//                status = findViewById<CheckBox>(R.id.statusCheck)
//                status.setOnCheckedChangeListener { compoundButton, b ->
//                    if(status.isChecked){
//                        statusValue[position] = 1
//                        adapter!!.notifyItemChanged(position)
//                        dbHelper!!.CompletedStatus(TaskName[position].toString())
//                    }else if (!status.isChecked){
//                        statusValue.set(position,0)
//                        adapter!!.notifyItemChanged(position)
//                        dbHelper!!.InCompleteStatus(TaskName[position].toString())
//                    }
//                }
//                status.setOnClickListener { b->
//
//                    if (status.isChecked) {
//                        Toast.makeText(applicationContext,"Status",Toast.LENGTH_SHORT).show()
//                        val name = TaskName!![position]
//                        dbHelper!!.CompletedStatus(name)
//                        TaskNameComplete.add(TaskName[position])
//                        initialValueComplete!!.add(initialValue!![position])
//                        finalValueComplete!!.add(finalValue!![position])
//                        dateValueComplete?.add(dateValue!![position])
//                        timeValueComplete?.add(timeValue!![position])
//                        reminderValueComplete?.add(reminderValue!![position])
//                        statusValueComplete.add(1)
//                        adapterComplete!!.notifyItemInserted(TaskNameComplete.size-1)
//
//                        statusValue.removeAt(position)
//                        TaskName!!.removeAt(position)
//                        initialValue!!.removeAt(position)
//                        finalValue!!.removeAt(position)
//                        dateValue?.removeAt(position)
//                        timeValue?.removeAt(position)
//                        reminderValue?.removeAt(position)
//
//                        adapter!!.notifyDataSetChanged()
//                    }else if(!status.isChecked){
//                        val name = TaskNameComplete!![position]
//                        dbHelper!!.InCompleteStatus(name)
//
//                        TaskName!!.add(TaskNameComplete[position])
//                        initialValue!!.add(initialValueComplete!![position])
//                        finalValue!!.add(finalValueComplete!![position])
//                        dateValue?.add(dateValueComplete!![position])
//                        timeValue?.add(timeValueComplete!![position])
//                        reminderValue?.add(reminderValueComplete!![position])
//                        statusValue.add(0)
//
//                        statusValueComplete.removeAt(position)
//                        TaskNameComplete.removeAt(position)
//                        initialValueComplete!!.removeAt(position)
//                        finalValueComplete!!.removeAt(position)
//                        dateValueComplete?.removeAt(position)
//                        timeValueComplete?.removeAt(position)
//                        reminderValueComplete?.removeAt(position)
//
//                        adapter!!.notifyItemInserted(TaskName.size-1)
//                        adapterComplete!!.notifyDataSetChanged()
//
//                    }
//                }


                bottomNav2 = findViewById<BottomNavigationView>(R.id.bottomNav2)
                bottomNav.visibility = View.GONE
                bottomNav2.visibility = View.VISIBLE
                bottomNav2.setOnNavigationItemReselectedListener {
                    when (it.itemId) {
                        R.id.add -> {
                            var initial: Int = initialValue!![position]
                            val name = TaskName!![position]
                            initial++
                            dbHelper!!.update(name, initial)
                            initialValue!!.removeAt(position)
                            initialValue!!.add(position, initial)
                            adapter!!.notifyItemChanged(position)
                        }
                        R.id.edit -> {}
                        R.id.delete -> {
                            val name = TaskName!![position]
                            dbHelper!!.delete(name)
                            TaskName!!.removeAt(position)
                            initialValue!!.removeAt(position)
                            finalValue!!.removeAt(position)
                            dateValue?.removeAt(position)
                            timeValue?.removeAt(position)
                            reminderValue?.removeAt(position)
                            adapter!!.notifyItemRemoved(position)

                            bottomNav2.visibility = View.VISIBLE
                            bottomNav.visibility = View.GONE
                        }
                        R.id.minus -> {
                            var initial: Int = initialValue!![position]
                            val name = TaskName!![position]
                            initial--
                            dbHelper!!.update(name, initial)
                            initialValue!!.removeAt(position)
                            initialValue!!.add(position, initial)
                            adapter!!.notifyItemChanged(position)
                        }

                    }
                }
                bottomNav.visibility = View.GONE
                bottomNav2.visibility = View.VISIBLE

            }

//            override fun onItemClick(view: View?, position: Int) {
//
//            }
        }

    }

}
