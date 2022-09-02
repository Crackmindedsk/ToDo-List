package com.sharlene.todolist.model

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.icu.text.CaseMap
import android.util.SparseBooleanArray
import android.view.*
import android.widget.*
import androidx.annotation.MenuRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.sharlene.todolist.*
import java.util.concurrent.TimeUnit

class TaskAdapter(val context: Context,
                  var taskname:ArrayList<*>,
                  var initialValue:ArrayList<*>,
                  var finalValue:ArrayList<*>,
                  var dateValue:ArrayList<*>,
                  var timeValue: ArrayList<*>,
                  var reminderValue: ArrayList<String>,
                  var statusValue: ArrayList<Int>,
                  private val listener: RecyclerViewClickListener?)
    :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val VIEW_TYPE_DATE = 1
    private val VIEW_TYPE_EMPTY = 2
    val timeActivity:AddTaskActivity = AddTaskActivity()
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
        var reminderIcon: ImageButton
        override fun onClick(p0: View?) {
//            if (p0 == card){
                listener?.onClick(itemView, adapterPosition)
            listener?.onClick(status,adapterPosition)
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
            reminderIcon = itemView.findViewById(R.id.reminder_icon)
            itemView.setOnClickListener(this)
            status.setOnClickListener(this)
            reminderIcon.setOnClickListener(this)

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
        holder.process.max = finalValue[position].toString().toInt()
        holder.dateTime.text = dateValue[position].toString() + ", " + timeValue[position].toString()
        holder.reminder.text = reminderValue[position].toString()

        if (initialValue[position] == finalValue[position]) {
            holder.status.isChecked = true
            holder.task.paintFlags = holder.task.paintFlags or STRIKE_THRU_TEXT_FLAG
            holder.track.text =
                initialValue[position].toString() + " / " + finalValue[position].toString()
            holder.process.progress = initialValue[position].toString().toInt()
            statusValue[position] = 1
            TaskDbHelper(context).CompletedStatus(taskname[position].toString())
        }
        else if(statusValue[position] == 1){
            holder.status.isChecked = true
            holder.task.paintFlags =  holder.task.paintFlags or STRIKE_THRU_TEXT_FLAG
            holder.track.text = finalValue[position].toString() + " / " + finalValue[position].toString()
            holder.process.progress = finalValue[position].toString().toInt()

        }else if(statusValue[position] == 0){
            holder.status.isChecked = false
            holder.task.paintFlags = holder.task.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            holder.track.text = initialValue[position].toString() + " / " + finalValue[position].toString()
            holder.process.progress = initialValue[position].toString().toInt()
        }
//        holder.status.isChecked = statusValue[position].toString().toInt() == 1

        holder.status.setOnCheckedChangeListener { compoundButton, b ->
            if(holder.status.isChecked){
                holder.task.paintFlags =  holder.task.paintFlags or STRIKE_THRU_TEXT_FLAG
                holder.track.text = finalValue[position].toString() + " / " + finalValue[position].toString()
                holder.process.progress = finalValue[position].toString().toInt()
                Add(statusValue, position, context)
            }else if (!holder.status.isChecked){
                holder.task.paintFlags = holder.task.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                holder.track.text = initialValue[position].toString() + " / " + finalValue[position].toString()
                holder.process.progress = initialValue[position].toString().toInt()
                Add(statusValue,position, context)
            }

        }

        holder.reminderIcon.setOnClickListener{v->
            var time:String = null.toString()
            val popup = PopupMenu(context!!, v)
            popup.menuInflater.inflate(R.menu.reminder_menu,popup.menu)
//        popup.menu.add(Menu.NONE,0,0,"5")
//        popup.menu.add(Menu.NONE,1,1,"30")
//        popup.menu.add(Menu.NONE,2,2,"1")

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                if (menuItem.itemId == R.id.five){
                    holder.reminder.text = menuItem.title.toString()
                    val Remindtime = (timeActivity.delayInSeconds) - (5/1000L)
                    ReminderAdd(reminderValue,position,context,menuItem.title.toString(),Remindtime)
                }else if (menuItem.itemId ==R.id.thirty){
                    holder.reminder.text = menuItem.title.toString()
                    val Remindtime = (timeActivity.delayInSeconds) - (5/1000L)
                    ReminderAdd(reminderValue,position,context,menuItem.title.toString(),Remindtime)
                }else if (menuItem.itemId == R.id.one){
                    holder.reminder.text = menuItem.title.toString()
                    val Remindtime = (timeActivity.delayInSeconds) - (5/1000L)
                    ReminderAdd(reminderValue,position,context,menuItem.title.toString(),Remindtime)
                }else if (menuItem.itemId == R.id.custom){
                    holder.reminder.text = menuItem.title.toString()
                    val Remindtime = (timeActivity.delayInSeconds) - (5/1000L)
                    ReminderAdd(reminderValue,position,context,menuItem.title.toString(),Remindtime)
                }
                false
                // Respond to menu item click.

            }
            popup.show()


//            val items = listOf("5 Minute before", "30 Minute before", "1 Hour Before", "Custom Time")
//            val adapter = ArrayAdapter.createFromResource(context,R.array.Reminder,
//                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
//            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
////            holder.reminder.text as? AutoCompleteTextView)?.setAdapter(adapter)
//            holder.spinner.adapter = adapter
//            time=holder.spinner
//            time = holder.reminder.text.toString()
//            ReminderAdd(reminderValue,position,context,time)
        }

    }

    fun ReminderAdd(reminderValue: ArrayList<String>,position: Int,context: Context,value: String, timeDelayInSeconds:Long ){
        reminderValue.set(position, value)
        Runnable({notifyItemChanged(position)})
        val str =taskname[position].toString()
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to str,
                    "message" to "Your have pending task $str"
                )
            )
            .build()
        WorkManager.getInstance(context).enqueue(myWorkRequest)
        TaskDbHelper(context).updateRemainder(taskname[position].toString(), value)
    }
    fun Add(statusValue: ArrayList<Int>,position: Int,context: Context){
        if(statusValue[position] == 0){
            statusValue.set(position,1)
            Runnable({ notifyItemChanged(position) })
            TaskDbHelper(context).CompletedStatus(taskname[position].toString())
        }else if(statusValue[position] == 1){
            statusValue.set(position,0)
            Runnable({ notifyItemChanged(position) })
            TaskDbHelper(context).InCompleteStatus(taskname[position].toString())
        }


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


