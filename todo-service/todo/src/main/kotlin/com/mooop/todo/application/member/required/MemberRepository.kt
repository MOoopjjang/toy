package com.mooop.todo.application.member.required

import com.mooop.todo.domain.member.Member
import com.mooop.todo.domain.member.MemberStatus
import org.springframework.data.repository.Repository

interface MemberRepository : Repository<Member, Long> {

    fun findById(memberId:Long):Member?

    fun findByUserid(userid:String):Member?

    fun findByStatus(status:MemberStatus):List<Member>

    fun save(member:Member):Member


}