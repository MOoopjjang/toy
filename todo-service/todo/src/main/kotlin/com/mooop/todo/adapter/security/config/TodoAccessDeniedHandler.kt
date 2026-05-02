package com.mooop.todo.adapter.security.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component


@Component
class TodoAccessDeniedHandler : AccessDeniedHandler {
    private val log = LoggerFactory.getLogger(TodoAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {

        log.error(">>>>>>>>>>>>>>>>>> 403 <<<<<<<<<<<<<<<<<<<<")
        response?.sendError(HttpStatus.FORBIDDEN.value() , "접근권한이 없습니다.")
    }
}