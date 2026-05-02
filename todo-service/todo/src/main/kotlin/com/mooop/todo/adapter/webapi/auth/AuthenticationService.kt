package com.mooop.todo.adapter.webapi.auth

import com.mooop.todo.adapter.security.dto.JwtCreateRequest
import com.mooop.todo.adapter.security.JwtService
import com.mooop.todo.adapter.webapi.auth.dto.SignInRequest
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import com.mooop.todo.application.member.provided.MemberFinder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service


@Service
class AuthenticationService constructor(
    private val memberFinder: MemberFinder
) {
    fun signIn(signInRequest: SignInRequest): String?{
        return memberFinder.getMember(signInRequest.userId)?.let {
            if(signInRequest.password == it.password()){
                if(!it.isActivate()){
                    throw Exception(TodoError.E004.message)
                }
                JwtService.createToken(JwtCreateRequest.create(it , 3600))
            }else{
                throw Exception(TodoError.E003.message)
            }
        }
    }

    fun signOut(request: HttpServletRequest , response: HttpServletResponse): Boolean{
        if(SecurityContextHolder.getContext().authentication.isAuthenticated){
            SecurityContextLogoutHandler().logout(request , response , SecurityContextHolder.getContext().authentication)
        }
        return true
    }
}