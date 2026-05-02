package com.mooop.todo.application.todo.provided

import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoStatus

data class TodoUnregisterRequest (
    val memberId:Long,
//    var todoTitle:String?=null,
    var todoCategory: TodoCategory?=null,
    var todoStatus: TodoStatus?=null,
    var startDay:String?=null,
    var endDay:String?=null,
){

}
