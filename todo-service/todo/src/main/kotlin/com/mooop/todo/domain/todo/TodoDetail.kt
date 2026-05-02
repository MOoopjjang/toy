package com.mooop.todo.domain.todo

import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "todo_detail")
class TodoDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id:Long?=null
        /** 시작 예정 날짜 */
    private var registerStartAt: LocalDate
        /** 완료 예정 날짜 */
    private var registerCompleteAt: LocalDate
        /** 시작 날짜 */
    private var startedAt: LocalDate? = null
        /** 완료 날짜 */
    private var completedAt: LocalDate? = null

        /** 우선순위 : default -1 */
        @Embedded
    private var priority:Priority

        /** 진행률 ( % ) : default 0 */
        @Embedded
    private var progress:Progress

        /** 진행상태 : default READY */
        @Enumerated(EnumType.STRING)
    private var status:TodoStatus
        /** 지연사유 */
    private var comment:String? = null


    constructor(registerStartAt: LocalDate , registerCompleteAt: LocalDate , priority:Int?){
        this.registerStartAt = registerStartAt
        this.registerCompleteAt = registerCompleteAt
        this.priority = Priority(priority?:9999)
        this.status = TodoStatus.READY
        this.progress = Progress(0)
    }


    companion object {
        fun create(todoRegisterRequest: TodoRegisterRequest):TodoDetail{
            return TodoDetail(
                todoRegisterRequest.startAt,
                todoRegisterRequest.completeAt,
                todoRegisterRequest.priority
            )
        }
    }


    fun start(){
        if(this.status == TodoStatus.START){
            throw Exception("이미 시작한 todo 입니다.")
        }

        if(this.status == TodoStatus.COMPLETE){
            throw Exception("이미 완료된 todo 입니다.")
        }

        this.status = TodoStatus.START
        this.startedAt = LocalDate.now()
    }

    fun complete(){
        if(this.status != TodoStatus.START){
            throw Exception("시작한 todo 가 아닙니다.")
        }

        this.status = TodoStatus.COMPLETE
        this.progress = Progress(100)
        this.completedAt = LocalDate.now()
    }

    fun hold(comment:String?){

        this.status = TodoStatus.HOLD
        comment?.let { this.comment = it }
    }

    fun registerComment(comment:String){
        if(this.status != TodoStatus.EXPIRE && this.status != TodoStatus.HOLD){
            throw Exception("사유는 만료 상태기어가 중단 상태일경우에만 작성 가능합니다.")
        }
        this.comment = comment
    }

    fun changePriority(priority:Int){
        this.priority = Priority(priority)
    }

    fun changeProgress(progress:Int){
        if(this.status != TodoStatus.START){
            throw Exception("진행률 변경은 todo가 시작한 후에 변경 가능합니다.")
        }
        this.progress = Progress(progress)
        if(this.progress.isComplete()){
            this.status = TodoStatus.COMPLETE
        }
    }


    fun id():Long = this.id!!
    fun registerStartAt():LocalDate = this.registerStartAt
    fun registerCompleteAt():LocalDate = this.registerCompleteAt
    fun startedAt():LocalDate? = this.startedAt
    fun completedAt():LocalDate? = this.completedAt
    fun priority():Int = this.priority.priorityValue
    fun progress():Int = this.progress.progressValue
    fun status():TodoStatus = this.status
    fun comment():String? = this.comment


    fun isComplete(): Boolean = this.status == TodoStatus.COMPLETE
    fun isHold(): Boolean = this.status == TodoStatus.HOLD
    fun isDelay(): Boolean = this.status == TodoStatus.DELAY

    fun isADayBeforeExpiration(): Boolean{
        if(this.status == TodoStatus.COMPLETE){
            return false
        }

        val targetDay = LocalDate.now().minusDays(1)
        return targetDay.isEqual(this.registerCompleteAt)
    }

}
