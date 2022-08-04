package com.sharlene.todolist

import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlin.collections.ArrayList
import com.sharlene.todolist.TaskDbHelper
import com.sharlene.todolist.model.TaskAdapter
import java.util.jar.Attributes

class MainActivity : AppCompatActivity() {

    private var progressBarStatus = 0

    var dbHelper: TaskDbHelper? = null
    var TaskName: ArrayList<String>? = null
    var initialValue: ArrayList<Int>? = null
    var finalValue: ArrayList<Int>? = null
    lateinit var name:TextInputEditText
    var adapter:TaskAdapter?=null
    private var listener: TaskAdapter.RecyclerViewClickListener?= null
    lateinit var initial:TextInputEditText
    lateinit var final: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view)
        val viewList = findViewById<View>(R.id.recycler_view) as RecyclerView
        TaskName= ArrayList()
        initialValue= ArrayList()
        finalValue = ArrayList()
        dbHelper = TaskDbHelper(this)

//        if (!TaskName!!.isEmpty()){
            selectData()
            setOnClickListener()
//        }

        adapter=TaskAdapter(this, TaskName!!,initialValue!!,finalValue!!,listener)
        viewList.adapter=adapter
        viewList.layoutManager=LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { v ->
            val dialogView = layoutInflater.inflate(R.layout.activity_add_task, null)
            val customDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .show()
            val btDismiss = customDialog.findViewById<Button>(R.id.add_btn)
            name= customDialog.findViewById(R.id.edit1)
            initial = customDialog.findViewById(R.id.edit2)
            final = customDialog.findViewById(R.id.edit3)
            btDismiss.setOnClickListener {
                if(name.text.toString() == "" || initial.text.toString()=="" || final.text.toString()==""){
                    Toast.makeText(this,"Fill the required details",Toast.LENGTH_SHORT).show()
                }else {
                    storeData()
                    selectSingle(name!!.text.toString())
                    customDialog.dismiss()
                }
            }
        }


    }
    private fun storeData(){
        val db:SQLiteDatabase = dbHelper!!.writableDatabase
        val nameTask = name!!.text.toString()
        val initialTask = initial.text.toString().toInt()
        val finalTask = final.text.toString().toInt()
        dbHelper!!.insert(nameTask,initialTask,finalTask,db)

    }
    private fun selectData(){
        val db:SQLiteDatabase= dbHelper!!.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", arrayOf())
        cursor.moveToFirst()
        do {
            val taskn:String = cursor.getString(0)
            val taski:Int = cursor.getInt(1)
            val taskf:Int = cursor.getInt(2)
            TaskName!!.add(taskn)
            initialValue!!.add(taski)
            finalValue!!.add(taskf)
        }while (cursor.moveToNext())
    }
    private fun selectSingle(string: String){
        val db:SQLiteDatabase = dbHelper!!.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE task_name = '$string'", arrayOf())
        cursor.moveToFirst()
        do {
            val taskn:String = cursor.getString(0)
            val taski:Int = cursor.getInt(1)
            val taskf:Int = cursor.getInt(2)
            TaskName!!.add(taskn)
            initialValue!!.add(taski)
            finalValue!!.add(taskf)
        }while (cursor.moveToNext())
    }

    private fun setOnClickListener(){
        listener= object :TaskAdapter.RecyclerViewClickListener{
            override fun onClick(view: View?, position: Int) {
                val dialogView = layoutInflater.inflate(R.layout.changer_dialog_card, null)
                val customDialog = AlertDialog.Builder(this@MainActivity)
                    .setView(dialogView)
                    .show()
                val btnAdd = customDialog.findViewById<ImageButton>(R.id.add)
                val btnDelete = customDialog.findViewById<ImageButton>(R.id.delete)
                val btnMinus = customDialog.findViewById<ImageButton>(R.id.minus)
                btnAdd.setOnClickListener { h->
                    var initial:Int=initialValue!![position]
                    val name = TaskName!![position]
                    initial++
                    dbHelper!!.update(name,initial)
                    initialValue!!.removeAt(position)
                    initialValue!!.add(position,initial)
                    adapter!!.notifyItemChanged(position)

                }
                btnDelete.setOnClickListener { v->
                    val name=TaskName!![position]
                    dbHelper!!.delete(name)
                    TaskName!!.removeAt(position)
                    initialValue!!.removeAt(position)
                    finalValue!!.removeAt(position)
                    adapter!!.notifyDataSetChanged()

                    customDialog.dismiss()
                }
                btnMinus.setOnClickListener { v->
                    var initial:Int=initialValue!![position]
                    val name = TaskName!![position]
                    initial--
                    dbHelper!!.update(name,initial)
                    initialValue!!.removeAt(position)
                    initialValue!!.add(position,initial)
                    adapter!!.notifyItemChanged(position)
                }
            }
        }
    }

}