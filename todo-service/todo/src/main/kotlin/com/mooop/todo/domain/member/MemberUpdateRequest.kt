package com.mooop.todo.domain.member

data class MemberUpdateRequest(
    var email: String?=null
    ,var password: String?=null
    ,var address:String?=null
    ,var profileImage:String?=null

)
