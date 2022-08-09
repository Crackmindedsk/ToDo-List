package com.sharlene.todolist.model

import java.util.*

class Data{
    var taskname: String? = null
    var initialValue: Int? = null
    var finalValue: Int? = null
    var date: Date? = null
    var time:String? = null
    var reminder:String? =null

    constructor(taskname: String?, initialValue: Int?, finalValue: Int?, date: Date?, time:String?, reminder:String?){
        this.taskname= taskname
        this.initialValue=initialValue
        this.finalValue=finalValue
        this.date=date
        this.time=time
        this.reminder=reminder
    }
}
