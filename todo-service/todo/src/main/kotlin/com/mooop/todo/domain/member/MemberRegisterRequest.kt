package com.mooop.todo.domain.member

data class MemberRegisterRequest(
    val email:String,
    val userid:String,
    val password:String,
    var address:String?,
    var profileImage:String?
)
