package com.sharlene.todolist.model

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharlene.todolist.R

class CompleteTaskAdapter(private val context: Context,
                  var taskname:ArrayList<*>,
                  var initialValue:ArrayList<*>,
                  var finalValue:ArrayList<*>,
                  var dateValue:ArrayList<*>,
                  var timeValue: ArrayList<*>,
                  var reminderValue: ArrayList<*>,
                  var statusValue: ArrayList<*>,
                  private val listenerComplete: RecyclerViewClickListener?)
    : RecyclerView.Adapter<CompleteTaskAdapter.CompleteTaskViewHolder>() {

    private val VIEW_TYPE_DATE = 1
    private val VIEW_TYPE_EMPTY = 2
    var checkBoxStateArray = SparseBooleanArray()

    inner class CompleteTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var task: TextView
        var track: TextView
        var process: ProgressBar
        var dateTime: TextView
        var reminder: TextView
        var status: CompoundButton
        override fun onClick(p0: View?) {
            listenerComplete?.onClick(itemView, adapterPosition)
        }

        init {
            task = itemView.findViewById(R.id.Text1)
            track = itemView.findViewById(R.id.Text2)
            process = itemView.findViewById(R.id.progressBar1)
            dateTime = itemView.findViewById(R.id.date_time)
            reminder = itemView.findViewById(R.id.reminder)
            status = itemView.findViewById(R.id.statusCheck)
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompleteTaskAdapter.CompleteTaskViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.progress_card, parent, false)
        return CompleteTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder:CompleteTaskViewHolder, position: Int) {
        holder.task.text = taskname[position].toString()
        holder.track.text =
            initialValue[position].toString() + " / " + finalValue[position].toString()
        holder.process.progress = initialValue[position].toString().toInt()
        holder.process.max = finalValue[position].toString().toInt()
        holder.dateTime.text = dateValue[position].toString() + ", " + timeValue[position].toString()
        holder.reminder.text = reminderValue[position].toString()
//        if (!checkBoxStateArray.get(position,false)){
//            holder.status.isChecked = false
//        }else{
//            holder.status.isChecked = true
//        }
        holder.status.isChecked = statusValue[position].toString().toInt() == 1


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


