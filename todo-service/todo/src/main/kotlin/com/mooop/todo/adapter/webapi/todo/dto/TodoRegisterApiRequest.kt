package com.mooop.todo.adapter.webapi.todo.dto


data class TodoRegisterApiRequest  constructor(
    val memberId:Long,
    val title:String,
    val content:String,
    val category: String,
    val startAt: String,
    val completeAt: String,
    var priority:Int = -1
){

}