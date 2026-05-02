package com.mooop.todo.adapter.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource


@Configuration
class SecurityConfig constructor(
    private val corsConfigurationSource: CorsConfigurationSource,
    private val tokenFilter: TokenFilter,
    private val todoAuthenticationEntryPoint: TodoAuthenticationEntryPoint,
    private val todoAccessDeniedHandler: TodoAccessDeniedHandler
){

    private val permitAllUri = arrayOf(
        "/error"
        ,"/auth/api/**"
        , "/member/register"
        , "/member/activate/**"
    )


    @Bean
    fun webSecurityCustomizer():WebSecurityCustomizer{
        return WebSecurityCustomizer{
            it.ignoring().requestMatchers("/res/**")
        }

    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain{
        httpSecurity.csrf { csrf->
            csrf.disable()
        }
            .cors { cors->cors.configurationSource(corsConfigurationSource) }
            .authorizeHttpRequests {
                it.requestMatchers(*permitAllUri).permitAll()
//                it.requestMatchers("/**").permitAll()
                    .anyRequest().authenticated()
            }
            .headers {
                it.frameOptions { fo->fo.sameOrigin() }
            }
            .sessionManagement { sessionManagementCustomizer->
                sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { exceptionHandle->
                /** 401 */
                exceptionHandle.authenticationEntryPoint(todoAuthenticationEntryPoint)
                /** 403 */
                exceptionHandle.accessDeniedHandler(todoAccessDeniedHandler)
            }
            .addFilterBefore( tokenFilter , UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic { it.disable() }
        return httpSecurity.build()
    }
}