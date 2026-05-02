package com.mooop.todo.adapter.webapi.todo.dto

import com.mooop.todo.domain.todo.TodoDetail
import com.mooop.todo.domain.todo.TodoStatus
import java.time.LocalDate

class TodoDetailInfo constructor(
    val detailId:Long,
    val registerStartAt: LocalDate,
    val registerCompleteAt: LocalDate,
    val startedAt: LocalDate?,
    val completedAt: LocalDate?,
    val priority:Int,
    val progress:Int,
    val status:TodoStatus,
    val comment:String?
){

    companion object{
        fun create(todoDetail:TodoDetail):TodoDetailInfo =
            TodoDetailInfo(
                todoDetail.id(),
                todoDetail.registerStartAt(),
                todoDetail.registerCompleteAt(),
                todoDetail.startedAt(),
                todoDetail.completedAt(),
                todoDetail.priority(),
                todoDetail.progress(),
                todoDetail.status(),
                todoDetail.comment()
            )
    }

}
