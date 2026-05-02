package com.mooop.todo.adapter.webapi.member.dto

data class PhotoUploadResponse(
    val originalFileName:String,
    val matchFileName:String,
    val url:String
)
