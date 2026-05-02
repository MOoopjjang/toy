package com.mooop.todo.application.todo.provided

import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.domain.todo.Todo

interface TodoFinder {

    fun getTodo(todoId:Long): Todo?

    fun findTodos(todoFinderRequest:TodoFindRequest):List<Todo>

    fun getCategory():List<String>

    fun getTodoStatus():List<String>
}