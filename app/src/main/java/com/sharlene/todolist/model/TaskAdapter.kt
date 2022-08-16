package com.sharlene.todolist.model

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sharlene.todolist.R
import com.sharlene.todolist.TaskDbHelper

class TaskAdapter(val context: Context,
                  var taskname:ArrayList<*>,
                  var initialValue:ArrayList<*>,
                  var finalValue:ArrayList<*>,
                  var dateValue:ArrayList<*>,
                  var timeValue: ArrayList<*>,
                  var reminderValue: ArrayList<*>,
                  var statusValue: ArrayList<Int>,
                  private val listener: RecyclerViewClickListener?)
    :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val VIEW_TYPE_DATE = 1
    private val VIEW_TYPE_EMPTY = 2
    var checkBoxStateArray = SparseBooleanArray()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var task: TextView
        var track: TextView
        var process: ProgressBar
        var dateTime: TextView
        var reminder: TextView
        var status:CheckBox
        var card:CardView
        override fun onClick(p0: View?) {
//            if (p0 == card){
                listener?.onClick(itemView, adapterPosition)
//            }else if(p0 == status){
//                listener?.onItemClick(status,adapterPosition)
//            }

        }

        init {
            task = itemView.findViewById(R.id.Text1)
            track = itemView.findViewById(R.id.Text2)
            process = itemView.findViewById(R.id.progressBar1)
            dateTime = itemView.findViewById(R.id.date_time)
            reminder = itemView.findViewById(R.id.reminder)
            status = itemView.findViewById(R.id.statusCheck)
            card = itemView.findViewById(R.id.card)
            itemView.setOnClickListener(this)
            status.setOnClickListener(this)

//            status.setOnClickListener {
//                if (!checkBoxStateArray.get(adapterPosition,false)){
//                    status.isChecked = true
//                    checkBoxStateArray.put(adapterPosition,true)
//                }else{
//                    status.isChecked = false
//                    checkBoxStateArray.put(adapterPosition,false)
//                }
//            }
        }

    }





    interface RecyclerViewClickListener {
        fun onClick(view: View?, position: Int)
//        fun onItemClick(view: View?, position: Int)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {

            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.progress_card, parent, false)
            return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.task.text = taskname[position].toString()
        holder.track.text =
            initialValue[position].toString() + " / " + finalValue[position].toString()
        holder.process.progress = initialValue[position].toString().toInt()
        holder.process.max = finalValue[position].toString().toInt()
        holder.dateTime.text = dateValue[position].toString() + ", " + timeValue[position].toString()
        holder.reminder.text = reminderValue[position].toString()
        if(statusValue[position] == 1){
            holder.status.isChecked = true
            holder.task.paintFlags =  holder.task.paintFlags or STRIKE_THRU_TEXT_FLAG
        }else{
            holder.status.isChecked = false
            holder.task.paintFlags = holder.task.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
//        holder.status.isChecked = statusValue[position].toString().toInt() == 1

        holder.status.setOnCheckedChangeListener { compoundButton, b ->
            if(holder.status.isChecked){
                holder.task.paintFlags =  holder.task.paintFlags or STRIKE_THRU_TEXT_FLAG
                statusValue.set(position,1)
                notifyItemChanged(position)
                TaskDbHelper(context).CompletedStatus(taskname[position].toString())
            }else if (!holder.status.isChecked){
                holder.task.paintFlags = holder.task.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                statusValue.set(position,0)
                notifyItemChanged(position)
                TaskDbHelper(context).InCompleteStatus(taskname[position].toString())
            }
        }


//        holder.status.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//
//            }
//
//        })

    }

    fun Add(statusValue: ArrayList<Int>,position: Int,context: Context){
        statusValue.add(position,1)
        val db : Unit = TaskDbHelper(context).CompletedStatus(taskname[position].toString())

    }

    override fun getItemCount(): Int {
        if (taskname.size == 0) {
            return 1
        } else {
            return taskname.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (taskname.size == 0) {
            return VIEW_TYPE_EMPTY
        } else {
            return VIEW_TYPE_DATE
        }
    }

}

private fun <E> ArrayList<E>.add(index: E, element: E) {

}

private fun CheckBox.setOnCheckedChangeListener(status: CheckBox, b: Boolean) {

}


