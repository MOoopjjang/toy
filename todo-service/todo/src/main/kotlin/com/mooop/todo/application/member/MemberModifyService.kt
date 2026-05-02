package com.mooop.todo.application.member

import com.mooop.todo.application.member.provided.MemberFinder
import com.mooop.todo.application.member.provided.MemberRegister
import com.mooop.todo.application.member.required.MemberRepository
import com.mooop.todo.domain.member.Member
import com.mooop.todo.domain.member.MemberRegisterRequest
import com.mooop.todo.domain.member.MemberUpdateRequest
import org.springframework.stereotype.Component

@Component
class MemberModifyService constructor(
    val memberFinder: MemberFinder
    ,val repository: MemberRepository
) : MemberRegister {
    override fun register(memberRegisterRequest: MemberRegisterRequest):Member {

        memberFinder.getMember(memberRegisterRequest.userid)?.let {
            throw Exception("이미 존재하는 아이디 입니다.")
        }

        return Member.create(memberRegisterRequest)
            .let {
                repository.save(it)
            }
    }

    override fun activate(memberId: Long):Member {
       return memberFinder.getMember(memberId)?.let {
           it.activate()
           repository.save(it)
       }?:throw Exception("member not found!!!")

    }

    override fun withdraw(memberId: Long):Member {
        return memberFinder.getMember(memberId)?.let {
            it.withdraw()
            repository.save(it)
        }?:throw Exception("member not found!!!")
    }

    override fun updateInfo(
        memberId: Long,
        memberUpdateRequest: MemberUpdateRequest
    ) : Member {
       return memberFinder.getMember(memberId)?.let {
           it.updateInfo(memberUpdateRequest)
           repository.save(it)
       }?:throw Exception("member not found!!!")
    }
}