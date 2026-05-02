package com.mooop.todo.application.todo

import com.mooop.todo.application.member.provided.MemberFinder
import com.mooop.todo.application.shared.EmailSendRequest
import com.mooop.todo.application.shared.required.EmailSender
import com.mooop.todo.application.todo.provided.TodoFindRequest
import com.mooop.todo.application.todo.provided.TodoFinder
import com.mooop.todo.application.todo.provided.TodoModifier
import com.mooop.todo.application.todo.provided.TodoUnregisterRequest
import com.mooop.todo.application.todo.required.TodoRepository
import com.mooop.todo.domain.todo.Todo
import com.mooop.todo.domain.todo.TodoCategory
import com.mooop.todo.domain.todo.TodoContentChangeRequest
import com.mooop.todo.domain.todo.TodoRegisterRequest
import org.springframework.stereotype.Component


@Component
class TodoModifierService constructor(
    val finder: TodoFinder
    ,val repository: TodoRepository
    ,val emailSender: EmailSender
    ,val memberFinder: MemberFinder
) : TodoModifier {
    override fun register(todoRegisterRequest: TodoRegisterRequest): Todo {
        return Todo.create(todoRegisterRequest).let{

            repository.save(it)
        }
    }

    override fun complete(todoId: Long): Todo {
        return finder.getTodo(todoId)?.let {
            it.detail().complete()
            val r = repository.save(it)
            sendEmail(r.memberId() , "완료" , r.title().plus(""))
            r

        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun start(todoId: Long): Todo {
        return finder.getTodo(todoId)?.let {
            it.detail().start()
            val r = repository.save(it)
            sendEmail(r.memberId() ,  "시작" , r.title().plus(""))
            r

        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun hold(todoId: Long , comment:String?): Todo {
        return finder.getTodo(todoId)?.let {
            it.detail().hold(comment)
            val r = repository.save(it)
            sendEmail(r.memberId() ,  "중단" , r.title().plus(""))
            r

        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun changePriority(todoId: Long, priority: Int):Todo {
        return finder.getTodo(todoId)?.let {
            val beforePriority = it.detail().priority()
            it.detail().changePriority(priority)
            val r = repository.save(it)
            sendEmail(r.memberId() ,  "우선순위 변경" , it.title().plus("이 "+beforePriority+" -> "+priority+" 로 변경되었습니다."))
            r
        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun changeProgress(todoId: Long, progress: Int):Todo {
        return finder.getTodo(todoId)?.also {
            val beforeProgress = it.detail().progress()
            it.detail().changePriority(progress)
            val r = repository.save(it)

            sendEmail(r.memberId() ,  "진행률 변경" , it.title().plus("이 "+beforeProgress+" -> "+progress+" 로 변경되었습니다."))
            r
        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun changeCategory(
        todoId: Long,
        category: TodoCategory
    ): Todo {
        return finder.getTodo(todoId)?.also {
            it.changeCategory(category)
            repository.save(it)
        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun changeContent(todoId: Long, todoContentChangeRequest:TodoContentChangeRequest): Todo {
        return finder.getTodo(todoId)?.let {
            it.changeContent(todoContentChangeRequest)
            val r = repository.save(it)
            sendEmail(r.memberId() ,  "내용이 변경되었습니다." , it.title().plus(""))
            r

        }?:throw Exception("content가 존재하지 않습니다.")
    }

    override fun unregister(todoId: Long) {
        finder.getTodo(todoId)?.let {
            repository.delete(it)
        }
    }

    override fun unregister(todoUnregisterRequest: TodoUnregisterRequest) {
        val allTodos = finder.findTodos(TodoFindRequest(memberId = todoUnregisterRequest.memberId , page = 1 , size = -1))
        if(allTodos.isNotEmpty()){
            // filter : TodoStatus
            val f1 = todoUnregisterRequest.todoStatus?.let { status->
                allTodos.filter { it.detail().status() == status }
            }?:allTodos

            // filter : TodoCategory
            val f2 = todoUnregisterRequest.todoCategory?.let { category->
                f1.filter { it.category() == category }
            }?:f1

           // TODO : 기간 filter 추가필요...

            if(f2.isNotEmpty()){
                f2.forEach { it->repository.delete(it) }
            }
        }

    }

    private fun sendEmail(memberId:Long , title:String , content:String){
        memberFinder.getMember(memberId)?.let { memberInfo->
            emailSender.send(EmailSendRequest("no-reply@todo.com",memberInfo.email(),title , content))
        }
    }
}