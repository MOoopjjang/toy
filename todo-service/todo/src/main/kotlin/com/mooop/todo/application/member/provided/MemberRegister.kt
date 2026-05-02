package com.mooop.todo.application.member.provided

import com.mooop.todo.domain.member.Member
import com.mooop.todo.domain.member.MemberRegisterRequest
import com.mooop.todo.domain.member.MemberUpdateRequest

interface MemberRegister {

    fun register(memberRegisterRequest: MemberRegisterRequest): Member

    fun activate(memberId:Long):Member

    fun withdraw(memberId:Long):Member

    fun updateInfo(memberId:Long , memberUpdateRequest: MemberUpdateRequest): Member
}