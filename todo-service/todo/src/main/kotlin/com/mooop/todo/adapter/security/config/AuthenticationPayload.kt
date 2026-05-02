package com.mooop.todo.adapter.security.config

data class AuthenticationPayload(
    val role:String,
    val username:String,
    val userid:String,
    val uid:String
)
