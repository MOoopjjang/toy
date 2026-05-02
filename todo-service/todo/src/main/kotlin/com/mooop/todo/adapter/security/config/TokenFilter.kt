package com.mooop.todo.adapter.security.config

import com.mooop.todo.adapter.security.JwtService
import com.mooop.todo.adapter.security.MemberAuthenticationProvider
import com.mooop.todo.adapter.shared.HttpUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenFilter constructor(
    private val memberAuthenticationProvider: MemberAuthenticationProvider
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(TokenFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if(isAllowRequest(request)){
            setAuthentication(request)
        }
        filterChain.doFilter(request,response)
    }


    private fun isAllowRequest(request: HttpServletRequest):Boolean{
        val xAuthToken = HttpUtil.extractHeaderValue( request , "x-auth-token")
        log.info(">>>>>>>>>>>>>>>> xAuthToken = {}",xAuthToken)
        if(StringUtils.isEmpty(xAuthToken)){
            return false
        }
        return JwtService.Companion.validationCheck(xAuthToken)
    }

    private fun setAuthentication(request: HttpServletRequest){
        val xAuthToken = HttpUtil.extractHeaderValue( request , "x-auth-token")
        if(xAuthToken.isNotBlank()){
            SecurityContextHolder.getContext().authentication = JwtService.getTokenPayload(xAuthToken)?.let { payload->
                UsernamePasswordAuthenticationToken(
                    payload.userid , "" , listOf(SimpleGrantedAuthority("ROLE_".plus(payload.role)))
                )
            }

            /** Adapter level에서 사용될 authenticaion 정보 등록 */
            memberAuthenticationProvider.register(xAuthToken)
        }



    }
}