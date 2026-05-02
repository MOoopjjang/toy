package com.mooop.todo.adapter.webapi.presentation

import com.mooop.todo.adapter.webapi.RestApiResponse
import com.mooop.todo.adapter.webapi.presentation.dto.TodoHeader
import com.mooop.todo.adapter.webapi.presentation.dto.TodoMain
import com.mooop.todo.adapter.webapi.todo.TodoService
import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.application.todo.provided.TodoFinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class TodoComponentApi constructor(
    private val todoComponentService: TodoComponentService
    ,private val todoService: TodoService
    ,private val todoFinder: TodoFinder
){


    @GetMapping("/main")
    fun main(): RestApiResponse<TodoMain>{
        return try{
            todoComponentService.createHeaderInfo().let {
                val todoList = todoService.findTodoInfo(TodoFindRequest(memberId = it.memberId , page = 1 , size = 100))
                RestApiResponse.ok(TodoMain(it , todoList))
            }
        }catch (e: Exception){
            RestApiResponse.fail(e.message)
        }
    }

    @GetMapping("/header")
    fun header():RestApiResponse<TodoHeader>{
        return try{
            todoComponentService.createHeaderInfo().let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            RestApiResponse.fail(e.message)
        }
    }
}