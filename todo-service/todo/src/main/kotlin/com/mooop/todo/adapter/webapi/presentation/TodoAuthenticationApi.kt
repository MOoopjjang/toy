package com.mooop.todo.adapter.webapi.presentation

import com.mooop.todo.adapter.webapi.RestApiResponse
import com.mooop.todo.adapter.webapi.auth.AuthenticationService
import com.mooop.todo.adapter.webapi.auth.dto.SignInRequest
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value =["/auth/api"])
class TodoAuthenticationApi constructor(
    val authenticationService: AuthenticationService
){

    @PostMapping("/signIn")
    fun signIn(@RequestBody signInRequest: SignInRequest): RestApiResponse<String> {
        return try{
            authenticationService.signIn(signInRequest)?.let {
                RestApiResponse.ok(it)
            }?: RestApiResponse.fail(TodoError.E003.message)
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }

    @GetMapping("/signOut")
    fun signOut(request: HttpServletRequest, response: HttpServletResponse): RestApiResponse<Boolean> {
        return authenticationService.signOut(request , response).let {
            RestApiResponse.ok(it)
        }
    }

}