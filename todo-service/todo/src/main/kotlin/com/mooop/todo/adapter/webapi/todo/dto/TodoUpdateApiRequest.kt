package com.mooop.todo.adapter.webapi.todo.dto

import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoStatus

data class TodoUpdateApiRequest (
    val memberId:Long,
    val todoId:Long,
    var title:String?=null,
    var content:String?=null,
    var category: TodoCategory?=null,
    var status: TodoStatus?=null,
    var startAt: String?=null,
    var completeAt: String?=null,
    var priority:Int?=null,
    var comment:String?=null

)