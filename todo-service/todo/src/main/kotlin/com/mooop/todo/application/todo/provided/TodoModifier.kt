package com.mooop.todo.application.todo.provided

import com.mooop.todo.domain.todo.Todo
import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoContentChangeRequest
import com.mooop.todo.domain.todo.TodoRegisterRequest

interface TodoModifier {

    fun register(todoRegisterRequest: TodoRegisterRequest) : Todo

    fun complete(todoId:Long) : Todo

    fun start(todoId:Long) : Todo

    fun hold(todoId: Long , comment:String?) : Todo

    fun changePriority(todoId:Long , priority:Int):Todo

    fun changeProgress(todoId:Long , progress:Int):Todo

    fun changeCategory(todoId:Long , category: TodoCategory):Todo

    fun changeContent(todoId: Long , todoContentChangeRequest:TodoContentChangeRequest):Todo

    fun unregister(todoId:Long)

    fun unregister(todoUnregisterRequest:TodoUnregisterRequest)

}