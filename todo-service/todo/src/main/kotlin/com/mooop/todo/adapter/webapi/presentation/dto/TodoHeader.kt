package com.mooop.todo.adapter.webapi.presentation.dto

class TodoHeader constructor(
    val userId:String,
    val memberId:Long,
    var profileImage:String?,
    val completeCount:Int,
    val ongoingCount:Int,
    val expireCount:Int,
    val holdCount:Int,
    val readyCount:Int
){


}
