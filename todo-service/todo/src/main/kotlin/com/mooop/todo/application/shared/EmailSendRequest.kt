package com.mooop.todo.application.shared

data class EmailSendRequest(
    val from:String,
    val to:String,
    val title:String,
    val body:String
)
