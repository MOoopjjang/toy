package com.mooop.todo.domain.todo

import jakarta.persistence.*


@Entity
@Table(name = "todo")
class Todo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id:Long? = null


    private var memberId:Long? = null

    private var title:String

    private var content:String

    @Enumerated(EnumType.STRING)
    private var category:TodoCategory

    @OneToOne(fetch = FetchType.LAZY , cascade = [ CascadeType.ALL ])
    @JoinColumn(name = "TODO_DETAIL_ID")
    private  lateinit var detail: TodoDetail


//    private constructor()

    private constructor(memberId:Long , title:String ,  content:String , category: TodoCategory){
        this.memberId = memberId
        this.title = title
        this.content = content
        this.category = category
    }


    companion object {
        fun create(todoRegisterRequest:TodoRegisterRequest) : Todo{
           val todo = Todo(todoRegisterRequest.memberId , todoRegisterRequest.title , todoRegisterRequest.content , todoRegisterRequest.category)
            todo.detail = TodoDetail.create(todoRegisterRequest)
            return todo
        }
    }

    fun changeContent(todoContentChangeRequest:TodoContentChangeRequest){
        todoContentChangeRequest.title?.let { this.title = it }
        todoContentChangeRequest.content?.let { this.content = it }
    }

    fun changeCategory(category: TodoCategory){
        this.category = category
    }

    fun id():Long = this.id!!
    fun memberId():Long = this.memberId!!
    fun title():String = this.title
    fun content():String = this.content
    fun category(): TodoCategory = this.category
    fun detail() : TodoDetail = this.detail

}
