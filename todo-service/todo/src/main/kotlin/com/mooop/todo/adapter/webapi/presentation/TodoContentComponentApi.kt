package com.mooop.todo.adapter.webapi.presentation

import com.mooop.todo.adapter.webapi.RestApiResponse
import com.mooop.todo.adapter.webapi.presentation.dto.TodoDeleteRequest
import com.mooop.todo.adapter.webapi.todo.TodoService
import com.mooop.todo.adapter.webapi.todo.dto.TodoInfo
import com.mooop.todo.adapter.webapi.todo.dto.TodoRegisterApiRequest
import com.mooop.todo.adapter.webapi.todo.dto.TodoUpdateApiRequest
import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.application.todo.provided.TodoFinder
import com.mooop.todo.application.todo.provided.TodoModifier
import com.mooop.todo.application.todo.provided.TodoUnregisterRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/content"])
class TodoContentComponentApi constructor(
    private val todoService: TodoService
    ,private val todoFinder: TodoFinder
){
    @PostMapping("/register")
    fun register(@RequestBody todoRegisterApiRequest: TodoRegisterApiRequest): RestApiResponse<List<TodoInfo>>{
        return try{
            todoService.registerTodoInfo(todoRegisterApiRequest).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }

    @PostMapping("/remove")
    fun remove(@RequestBody todoDeleteRequest: TodoDeleteRequest): RestApiResponse<String>{
        return try{
            todoService.removeTodoInfo(todoDeleteRequest).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }



    @PostMapping("/update")
    fun update(@RequestBody todoUpdateApiRequest: TodoUpdateApiRequest): RestApiResponse<TodoInfo>{
        return try{
            todoService.updateTodoInfo(todoUpdateApiRequest).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }



    @PostMapping("/find-list")
    fun information(@RequestBody todoFindRequest: TodoFindRequest): RestApiResponse<List<TodoInfo>>{
        return try{
            todoService.findTodoInfo(todoFindRequest).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @GetMapping("/info-list")
    fun information():RestApiResponse<List<TodoInfo>>{
        return try{
            todoService.getTodoInfo().let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @GetMapping("/category")
    fun getTodoCategoryInfo(): RestApiResponse<List<String>>{
        return try{
            todoFinder.getCategory().let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }

    @GetMapping("/status")
    fun getTodoStatusInfo(): RestApiResponse<List<String>>{
        return try{
            todoFinder.getTodoStatus().let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }
}