package com.mooop.todo.domain.member

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.engine.internal.Cascade


/**
 *
 */
@Entity
@Table(name = "TODO_MEMBER")
class Member{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  var id:Long? = null
    @Embedded
    private var email: Email
    private var userid:String
    @Embedded
    private var password: Password
    @Enumerated(EnumType.STRING)
    private lateinit var status:MemberStatus
    @OneToOne(fetch = FetchType.LAZY , cascade = [CascadeType.ALL])
    @JoinColumn(name = "DETAIL_ID" )
    private lateinit var detail:MemberDetail


    constructor(email:Email , userid:String , password:Password){
        this.email = email
        this.userid = userid
        this.password = password
    }

    companion object{
        fun create(memberRegisterRequest:MemberRegisterRequest):Member{
            return  Member(Email(memberRegisterRequest.email) , memberRegisterRequest.userid , Password(memberRegisterRequest.password))
                .apply {
                    this.status = MemberStatus.DEACTIVATE
                    this.detail = MemberDetail.create(memberRegisterRequest)
                }
        }
    }

    fun activate(){
        if(this.status != MemberStatus.DEACTIVATE){
            throw Exception("")
        }
        this.status = MemberStatus.ACTIVATE
        this.detail.activate()
    }

    fun withdraw(){
        if(this.status != MemberStatus.ACTIVATE){
            throw Exception("")
        }
        this.status = MemberStatus.WITHDRAW
        this.detail.withdraw()
    }

    fun isActivate():Boolean{
        return this.status == MemberStatus.ACTIVATE
    }

    fun updateInfo(memberUpdateRequest:MemberUpdateRequest){
        memberUpdateRequest.email?.let { this.email = Email(it) }
        memberUpdateRequest.password?.let { this.password = Password(it) }
        this.detail.updateInfo(memberUpdateRequest)
    }


    fun id():Long = this.id!!
    fun email():String = this.email.component1()
    fun userId():String = this.userid
    fun password():String = this.password.component1()
    fun status():MemberStatus = this.status

    fun detail():MemberDetail = this.detail

}