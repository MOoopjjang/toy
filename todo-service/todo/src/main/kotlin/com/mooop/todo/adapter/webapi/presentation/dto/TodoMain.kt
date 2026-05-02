package com.mooop.todo.adapter.webapi.presentation.dto

import com.mooop.todo.adapter.webapi.todo.dto.TodoInfo

class TodoMain constructor(
    val header: TodoHeader?,
    val content:List<TodoInfo>
)