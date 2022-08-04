package com.sharlene.todolist.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.sharlene.todolist.R
import java.util.ArrayList

class TaskAdapter(private val context: Context,
                  var taskname:ArrayList<*>,
                  var initialValue:ArrayList<*>,
                  var finalValue:ArrayList<*>,
                  private val listener: RecyclerViewClickListener?)
    :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
            View.OnClickListener{
                var task: TextView
                var track:TextView
                var process:ProgressBar
        override fun onClick(p0: View?) {
            listener?.onClick(itemView,adapterPosition)
        }
        init {
            task=itemView.findViewById(R.id.Text1)
            track=itemView.findViewById(R.id.Text2)
            process=itemView.findViewById(R.id.progressBar1)
            itemView.setOnClickListener(this)
        }
            }

    interface RecyclerViewClickListener {
        fun onClick(view: View?, position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.progress_card,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        if(!taskname.isEmpty()){
            holder.task.text = taskname[position].toString()
            holder.track.text = initialValue[position].toString() + " / " +finalValue[position].toString()
            holder.process.progress = initialValue[position].toString().toInt()
            holder.process.max = finalValue[position].toString().toInt()
//        }

    }

    override fun getItemCount(): Int {
        if(taskname.size == 0){
            return 1
        }else {
            return taskname.size
        }
    }
}


