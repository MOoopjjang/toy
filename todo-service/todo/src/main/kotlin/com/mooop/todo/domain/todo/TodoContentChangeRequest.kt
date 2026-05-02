package com.mooop.todo.domain.todo

data class TodoContentChangeRequest constructor(
    var content:String?,
    var title:String?
)
