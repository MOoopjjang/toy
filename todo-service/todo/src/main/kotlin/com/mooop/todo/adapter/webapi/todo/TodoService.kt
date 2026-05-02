package com.mooop.todo.adapter.webapi.todo

import com.mooop.todo.adapter.security.MemberAuthenticationProvider
import com.mooop.todo.adapter.shared.DateUtil
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import com.mooop.todo.adapter.webapi.presentation.dto.TodoDeleteRequest
import com.mooop.todo.adapter.webapi.todo.dto.TodoInfo
import com.mooop.todo.adapter.webapi.todo.dto.TodoRegisterApiRequest
import com.mooop.todo.adapter.webapi.todo.dto.TodoUpdateApiRequest
import com.mooop.todo.application.member.provided.MemberFinder
import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.application.todo.provided.TodoFinder
import com.mooop.todo.application.todo.provided.TodoModifier
import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoContentChangeRequest
import com.mooop.todo.domain.todo.TodoRegisterRequest
import com.mooop.todo.domain.todo.TodoStatus
import org.springframework.stereotype.Service

@Service
class TodoService constructor(
    private val todoModifier: TodoModifier
    ,private val todoFinder: TodoFinder
    ,private val memberFinder: MemberFinder
    ,private val memberAuthenticationProvider: MemberAuthenticationProvider
){
    fun registerTodoInfo( todoRegisterApiRequest: TodoRegisterApiRequest): List<TodoInfo> {

        val todoRegisterRequest = TodoRegisterRequest(
            todoRegisterApiRequest.memberId,
            todoRegisterApiRequest.title,
            todoRegisterApiRequest.content,
            TodoCategory.toEnum(todoRegisterApiRequest.category),
            DateUtil.convertStringToLD(todoRegisterApiRequest.startAt),
            DateUtil.convertStringToLD(todoRegisterApiRequest.completeAt),
            todoRegisterApiRequest.priority
        )
        return todoModifier.register(todoRegisterRequest).let {
            val todoInfo = TodoInfo.of(it)
            listOf(todoInfo)
        }
    }


    fun removeTodoInfo(todoDeleteRequest: TodoDeleteRequest):String{
        todoDeleteRequest.todoList.forEach { it->todoModifier.unregister(it) }
        return "OK"
    }


    // TODO : query 여러번 발생 별도의 update API 필요...
    fun updateTodoInfo( todoUpdateApiRequest: TodoUpdateApiRequest):TodoInfo{

        todoUpdateApiRequest.status?.let {
            if(it == TodoStatus.HOLD)todoModifier.hold(todoUpdateApiRequest.todoId , todoUpdateApiRequest.comment)
            else if(it == TodoStatus.START)todoModifier.start(todoUpdateApiRequest.todoId)
            else if(it == TodoStatus.COMPLETE)todoModifier.complete(todoUpdateApiRequest.todoId)
            else {
                // not working...
            }
        }

        if(todoUpdateApiRequest.title != null || todoUpdateApiRequest.content != null){
            todoModifier.changeContent(todoUpdateApiRequest.todoId , TodoContentChangeRequest(todoUpdateApiRequest.content , todoUpdateApiRequest.title))
        }

        todoUpdateApiRequest.priority?.let { todoModifier.changePriority(todoUpdateApiRequest.todoId , it) }

        return todoUpdateApiRequest.category?.let {
            val r = todoModifier.changeCategory(todoUpdateApiRequest.todoId , it)
            TodoInfo.of(r)
        }?:throw Exception(TodoError.E002.message)


        // TODO : 시작 ~ 종료 날짜 변경 코드 누락됨.!!!!
    }


    fun findTodoInfo(todoFindRequest: TodoFindRequest) : List<TodoInfo>{
        val todoList = todoFinder.findTodos(todoFindRequest)
        if(todoList.isEmpty()){
            return listOf()
        }

        return todoList.map { TodoInfo.of(it) }
    }


    fun getTodoInfo() : List<TodoInfo>{
        return memberAuthenticationProvider.getAuthenticationInfo()?.let {
            val memberInfo = memberFinder.getMember(it.userid)?:throw Exception(TodoError.E001.message)
            findTodoInfo(TodoFindRequest(memberId = memberInfo.id() , page = 1 , size = -1))
        }?:throw Exception(TodoError.E000.message)
    }

}