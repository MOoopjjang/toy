package com.mooop.todo.application.todo.required

import com.mooop.todo.domain.todo.Todo
import org.springframework.data.repository.Repository

interface TodoRepository : Repository<Todo , Long> {

    fun findById(todoId:Long):Todo?

    fun findByMemberId(memberId:Long):List<Todo>

    fun save(todo:Todo):Todo

    fun delete(todo:Todo)
}