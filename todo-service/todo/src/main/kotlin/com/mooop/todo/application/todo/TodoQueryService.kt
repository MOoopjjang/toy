package com.mooop.todo.application.todo

import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.application.todo.provided.TodoFinder
import com.mooop.todo.application.todo.required.TodoRepository
import com.mooop.todo.domain.todo.Todo
import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoStatus
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class TodoQueryService constructor(
    val repository: TodoRepository
) : TodoFinder {
    override fun getTodo(todoId: Long): Todo? {
        return repository.findById(todoId)
    }

    override fun findTodos(todoFinderRequest: TodoFindRequest): List<Todo> {

        return repository.findByMemberId(todoFinderRequest.memberId).let {
            if(it.isEmpty()){
                listOf()
            }else{
                val v = todoFinderRequest.todoId?.let{ tid->
                    it.first { todo -> todo!!.id() == tid }
                }
                if( v != null ){
                    return listOf(v)
                }


                // title 검색
                val l1 = todoFinderRequest.todoTitle?.let { title->
                    titleFilter(it , title)
                }?:it

                // category 검색
                val l2 = if(!l1.isEmpty() ){
                    todoFinderRequest.todoCategory?.let { category->
                        categoryFilter(l1 , category)
                    }?:l1
                }else{
                    listOf()
                }


                // 진행상태
                val l3 = if(!l2.isEmpty() ){
                    todoFinderRequest.todoStatus?.let { status->
                        statusFilter(l2 , status)
                    }?:l2
                }else{
                    listOf()
                }

                // 범위 검색 ( 시작일 ~ 종료일 )
                dayFilter(l3 , todoFinderRequest.startDay , todoFinderRequest.endDay)
            }

        }
    }

    override fun getCategory(): List<String> = TodoCategory.entries.map { it.name }

    override fun getTodoStatus(): List<String> = TodoStatus.entries.map { it.name }


    private fun titleFilter(todoInfos:List<Todo> , findTitle:String):List<Todo> = todoInfos.filter { it.title().contains(findTitle) }


    private fun categoryFilter(todoInfos:List<Todo> , findCategory: TodoCategory):List<Todo> = todoInfos.filter { it.category() == findCategory }


    private fun statusFilter(todoInfos:List<Todo> , todoStatus: TodoStatus):List<Todo> = todoInfos.filter { it.detail().status() == todoStatus }


    private fun dayFilter(todoInfos:List<Todo> , startDay:String?=null , endDay:String?=null):List<Todo>{

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var filterList = startDay?.let{
            if(it.isBlank()){
                todoInfos
            }else{
                val startDate = LocalDate.parse(it , formatter)
                todoInfos.filter {info-> !info.detail().registerStartAt().isBefore(startDate) }
            }
        }?:todoInfos

        filterList = endDay?.let{
            if(it.isBlank()){
                filterList
            }else{
                val endDate = LocalDate.parse(it , formatter)
                filterList.filter { info-> !info.detail().registerCompleteAt().isAfter(endDate) }
            }

        }?:filterList
        return filterList
    }
}