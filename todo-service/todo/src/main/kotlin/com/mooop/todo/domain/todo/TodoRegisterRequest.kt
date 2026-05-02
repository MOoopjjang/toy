package com.mooop.todo.domain.todo

import java.time.LocalDate

data class TodoRegisterRequest(

    val memberId:Long,
    val title:String,
    val content:String,
    val category: TodoCategory,
    val startAt: LocalDate,
    val completeAt: LocalDate,
    var priority:Int = -1
)
