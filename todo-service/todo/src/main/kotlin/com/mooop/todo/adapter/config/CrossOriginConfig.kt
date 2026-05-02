package com.mooop.todo.adapter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CrossOriginConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        return CorsConfiguration().let {
            it.allowedOriginPatterns = listOf("*")
            it.allowedMethods = listOf("GET" , "POST" , "PUT" , "DELETE" , "OPTION")
            it.allowedHeaders = listOf("*")
            it.allowCredentials = true

            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**" , it)
            }
        }
    }
}