package com.mooop.todo.adapter.webapi.presentation

import com.mooop.todo.adapter.security.MemberAuthenticationProvider
import com.mooop.todo.adapter.webapi.member.MemberService
import com.mooop.todo.adapter.webapi.member.dto.MemberInfo
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import com.mooop.todo.adapter.webapi.presentation.dto.TodoHeader
import com.mooop.todo.adapter.webapi.todo.TodoService
import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.domain.todo.TodoStatus
import org.springframework.stereotype.Service

@Service
class TodoComponentService constructor(
    private val memberAuthenticationProvider: MemberAuthenticationProvider,
    private val memberService: MemberService,
    private val todoService: TodoService
){

    fun createHeaderInfo(): TodoHeader{
        return memberAuthenticationProvider.getAuthenticationInfo()?.let {
            val memberInfo = memberService.getMemberInfoFromUserId()?:throw Exception(TodoError.E001.message)
            val todoList = todoService.findTodoInfo(TodoFindRequest(memberId = memberInfo.memberId, page = 1, size = -1))
            if(todoList.isEmpty()){
                return TodoHeader(
                    memberInfo.userid
                    ,memberInfo.memberId
                    ,memberInfo.profileImage?.let{ "http://localhost:8080".plus(it)}
                    ,0,0,0,0,0
                )
            }else{
                return TodoHeader(
                    memberInfo.userid
                    ,memberInfo.memberId
                    ,memberInfo.profileImage?.let{ "http://localhost:8080".plus(it)}
                    ,todoList.count { it.detail.status == TodoStatus.COMPLETE }
                    ,todoList.count { it.detail.status == TodoStatus.START }
                    ,todoList.count { it.detail.status == TodoStatus.EXPIRE }
                    ,todoList.count { it.detail.status == TodoStatus.HOLD }
                    ,todoList.count { it.detail.status == TodoStatus.READY }
                )

            }
        }?:throw Exception(TodoError.E000.message)

    }



//    fun getMemberInfo(): MemberInfo{
//        return memberAuthenticationProvider.getAuthenticationInfo()?.let{
//            memberService.getMemberInfoFromUserId(it.userid)?:throw Exception(TodoError.E001.message)
//        }?:throw Exception(TodoError.E000.message)
//    }

}