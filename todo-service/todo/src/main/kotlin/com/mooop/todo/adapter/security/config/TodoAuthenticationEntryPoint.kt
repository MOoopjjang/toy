package com.mooop.todo.adapter.security.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.Serializable


@Component
class TodoAuthenticationEntryPoint : AuthenticationEntryPoint , Serializable {

    private val log = LoggerFactory.getLogger(TodoAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        log.error(">>>>>>>>>>>>>>>>>> 401 <<<<<<<<<<<<<<<<<<<<")
        response?.sendError(HttpStatus.UNAUTHORIZED.value() , "token이 유효하지 않습니다.")
    }
}