package com.mooop.todo.application.member.provided

import com.mooop.todo.domain.member.Member

interface MemberFinder {
    fun getMember(memberId:Long): Member?
    fun getMember(userId:String): Member?
    fun findMembers(memberFindRequest:MemberFindRequest): List<Member>
}