package com.mooop.todo.adapter.security

import com.mooop.todo.adapter.security.config.AuthenticationPayload
import org.springframework.stereotype.Component

@Component
class MemberAuthenticationProvider {

    private val authenticationInfo: ThreadLocal<AuthenticationPayload> = ThreadLocal<AuthenticationPayload>()


    fun register(token:String){
        JwtService.getTokenPayload(token)?.let {
            unregister()
            authenticationInfo.set(it)
        }
    }

    fun unregister(){
        authenticationInfo.get()?.let {
            authenticationInfo.remove()
        }
    }


    fun getAuthenticationInfo():AuthenticationPayload? = authenticationInfo.get()


}