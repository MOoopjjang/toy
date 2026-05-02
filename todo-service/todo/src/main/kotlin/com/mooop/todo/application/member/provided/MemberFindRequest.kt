package com.mooop.todo.application.member.provided

import com.mooop.todo.domain.member.MemberStatus

data class MemberFindRequest constructor(
    var status:MemberStatus?
)
