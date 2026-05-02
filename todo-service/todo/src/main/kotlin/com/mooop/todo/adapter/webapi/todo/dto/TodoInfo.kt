package com.mooop.todo.adapter.webapi.todo.dto

import com.mooop.todo.domain.todo.Todo
import com.mooop.todo.domain.todo.TodoCategory

class TodoInfo constructor(
    val todoId:Long,
    val memberId:Long,
    val title:String,
    val content:String,
    val category: TodoCategory,
    val detail: TodoDetailInfo
){

    companion object{
        fun of(todo: Todo):TodoInfo{
            val detail = TodoDetailInfo.create(todo.detail())
            return TodoInfo(
                todo.id(),
                todo.memberId(),
                todo.title(),
                todo.content(),
                todo.category(),
                detail
            )
        }
    }

}
