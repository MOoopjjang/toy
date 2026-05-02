package com.mooop.todo.domain.member

import jakarta.persistence.Embeddable


@Embeddable
data class Email(val email:String){

    init{
        if(email.isBlank()){
            throw Exception("이메일값이 없습니다.")
        }

        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        if(!EMAIL_REGEX.matches(email)){
            throw Exception("이메일 형식에 맞지 않습니다.")
        }
    }


}
