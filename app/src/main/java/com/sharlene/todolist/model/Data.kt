package com.sharlene.todolist.model

class Data{
    var taskname: String? = null
    var initialValue: Int? = null
    var finalValue: Int? = null

    constructor(taskname: String?, initialValue: Int?, finalValue: Int?){
        this.taskname= taskname
        this.initialValue=initialValue
        this.finalValue=finalValue
    }
}
