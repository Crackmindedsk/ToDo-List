package com.sharlene.todolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.collections.ArrayList
import com.sharlene.todolist.TaskDbHelper
import com.sharlene.todolist.model.TaskAdapter
import java.text.SimpleDateFormat
import java.util.*
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
    var adapter: TaskAdapter? = null
    private var listener: TaskAdapter.RecyclerViewClickListener? = null
    lateinit var bottomNav: BottomNavigationView
    lateinit var bottomNav2: BottomNavigationView


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
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view)
        val viewList = findViewById<View>(R.id.recycler_view) as RecyclerView
        TaskName = ArrayList()
        initialValue = ArrayList()
        finalValue = ArrayList()
        dateValue = ArrayList()
        timeValue = ArrayList()
        reminderValue = ArrayList()
        dbHelper = TaskDbHelper(this)
        val empty: ConstraintLayout = findViewById(R.id.empty_view)

//            selectData()
//            setOnClickListener()


        if (dbHelper!!.readAll() == 0) {
            viewList.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            viewList.visibility = View.VISIBLE
            empty.visibility = View.GONE
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
                listener
            )
            viewList.adapter = adapter
            viewList.layoutManager = LinearLayoutManager(this)
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
            TaskName!!.add(taskn)
            initialValue!!.add(taski)
            finalValue!!.add(taskf)
            dateValue?.add(taskd)
            timeValue?.add(taskt)
            reminderValue?.add(taskr)
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

    private fun setOnClickListener() {
        listener = object : TaskAdapter.RecyclerViewClickListener {
            override fun onClick(view: View?, position: Int) {
//                val dialogView = layoutInflater.inflate(R.layout.changer_dialog_card, null)
//                val customDialog = AlertDialog.Builder(this@MainActivity)
//                    .setView(dialogView)
//                    .show()
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
                            adapter!!.notifyDataSetChanged()

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
        }

    }
}