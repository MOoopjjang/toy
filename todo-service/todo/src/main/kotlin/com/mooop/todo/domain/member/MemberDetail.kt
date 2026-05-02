package com.mooop.todo.domain.member

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime


@Table(name = "TODO_MEMBER_DETAIL")
@Entity
class MemberDetail {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id:Long? = null
    private var address:String?=null
    @Embedded
    private var profileImage:Image? = null
    private lateinit var registeredAt:LocalDateTime
    private var activatedAt:LocalDateTime? = null
    private var withdrawAt:LocalDateTime? = null



    constructor(address:String? , profileImage:Image?){
        this.address = address
        this.profileImage = profileImage
        this.registeredAt = LocalDateTime.now()
    }

    companion object{
        fun create(memberRegisterRequest:MemberRegisterRequest):MemberDetail{
            val pImage = if(memberRegisterRequest.profileImage == null){
                null
            }else{
                Image(memberRegisterRequest.profileImage!!)
            }
            return MemberDetail(memberRegisterRequest.address , pImage)
        }
    }

    fun activate(){
        this.activatedAt = LocalDateTime.now()
    }

    fun withdraw(){
        this.withdrawAt = LocalDateTime.now()
    }

    fun updateInfo(memberUpdateRequest: MemberUpdateRequest){
        memberUpdateRequest.profileImage?.let{
            this.profileImage = Image(it)
        }

        memberUpdateRequest.address?.let {
            this.address = it
        }
    }


    fun address():String? = this.address
    fun profileImage():String? = this.profileImage?.imageUrl
    fun registeredAt():LocalDateTime = this.registeredAt
    fun activatedAt():LocalDateTime? = this.activatedAt
    fun withdrawAt():LocalDateTime? = this.withdrawAt

}