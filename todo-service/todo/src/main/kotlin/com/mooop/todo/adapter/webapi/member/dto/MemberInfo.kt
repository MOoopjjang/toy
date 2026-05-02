package com.mooop.todo.adapter.webapi.member.dto

import com.mooop.todo.adapter.shared.DateUtil
import com.mooop.todo.domain.member.Member
import com.mooop.todo.domain.member.MemberStatus

class MemberInfo constructor(
    val memberId:Long,
    val email:String,
    val userid:String,
    val status:MemberStatus,
    val address:String?,
    val profileImage:String?,
    val registeredAt: String,
    val activatedAt: String?,
    val withdrawAt: String?
) {

    companion object{
        fun of(member: Member):MemberInfo{

            return MemberInfo(
                member.id()
                ,member.email()
                ,member.userId()
                ,member.status()
                ,member.detail().address()
                ,member.detail().profileImage()
                , DateUtil.convertLDTToString(member.detail().registeredAt())
                , member.detail().activatedAt()?.let { DateUtil.convertLDTToString(it) }
                , member.detail().withdrawAt()?.let { DateUtil.convertLDTToString(it) }
            )

        }
    }
}