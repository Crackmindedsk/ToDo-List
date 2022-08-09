package com.sharlene.todolist.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharlene.todolist.R

class TaskAdapter(private val context: Context,
                  var taskname:ArrayList<*>,
                  var initialValue:ArrayList<*>,
                  var finalValue:ArrayList<*>,
                  var dateValue:ArrayList<*>,
                  var timeValue: ArrayList<*>,
                  var reminderValue: ArrayList<*>,
                  private val listener: RecyclerViewClickListener?)
    :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val VIEW_TYPE_DATE = 1
    private val VIEW_TYPE_EMPTY = 2

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var task: TextView
        var track: TextView
        var process: ProgressBar
        var dateTime: TextView
        var reminder: TextView
        override fun onClick(p0: View?) {
            listener?.onClick(itemView, adapterPosition)
        }

        init {
            task = itemView.findViewById(R.id.Text1)
            track = itemView.findViewById(R.id.Text2)
            process = itemView.findViewById(R.id.progressBar1)
            dateTime = itemView.findViewById(R.id.date_time)
            reminder = itemView.findViewById(R.id.reminder)
            itemView.setOnClickListener(this)
        }
    }



    interface RecyclerViewClickListener {
        fun onClick(view: View?, position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.progress_card, parent, false)
            return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        if(!taskname.isEmpty()){
        holder.task.text = taskname[position].toString()
        holder.track.text =
            initialValue[position].toString() + " / " + finalValue[position].toString()
        holder.process.progress = initialValue[position].toString().toInt()
        holder.process.max = finalValue[position].toString().toInt()
        holder.dateTime.text =
            dateValue[position].toString() + ", " + timeValue[position].toString()
        holder.reminder.text = reminderValue[position].toString()
//        }

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


