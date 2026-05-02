package com.mooop.todo.application.member

import com.mooop.todo.application.member.provided.MemberFindRequest
import com.mooop.todo.application.member.provided.MemberFinder
import com.mooop.todo.application.member.required.MemberRepository
import com.mooop.todo.domain.member.Member
import org.springframework.stereotype.Component

@Component
class MemberQueryService constructor(
    val repository: MemberRepository
) : MemberFinder {


    override fun getMember(memberId: Long): Member? {
        return repository.findById(memberId)
    }

    override fun getMember(userId: String): Member? {
        return repository.findByUserid(userId)
    }

    override fun findMembers(memberFindRequest: MemberFindRequest): List<Member> {
        return repository.findByStatus(memberFindRequest.status!!)
    }
}