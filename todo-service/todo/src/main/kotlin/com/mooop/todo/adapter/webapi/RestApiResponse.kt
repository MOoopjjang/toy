package com.mooop.todo.adapter.webapi

class RestApiResponse<T> constructor(
     val result:String,
     var message:String?,
     var body:T?
){
    companion object{
        fun <T>ok(body:T?):RestApiResponse<T> = RestApiResponse("SUCCESS", null , body)

        fun <T>fail(message:String?):RestApiResponse<T> = RestApiResponse("FAIL", message , null)

    }
}