package com.mooop.todo.adapter.security.dto

import com.mooop.todo.adapter.security.config.AuthenticationPayload
import com.mooop.todo.domain.member.Member
import kotlin.random.Random

data class JwtCreateRequest(
    val expireSec:Int,
//    val secretKey: SecretKey,
    val payload: AuthenticationPayload
){

    companion object{
        fun create(member: Member, expireSec: Int ):JwtCreateRequest{
            return AuthenticationPayload("USER", "", member.userId(), Random(16).toString()).let {
                JwtCreateRequest(expireSec , it)
            }
        }
    }
}