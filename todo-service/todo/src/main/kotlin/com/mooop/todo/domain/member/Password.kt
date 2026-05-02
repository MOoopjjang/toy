package com.mooop.todo.domain.member

import jakarta.persistence.Embeddable

@Embeddable
data class Password(val password:String){
    init{
        if(password.isBlank()){
            throw Exception("암호를 입력해주세요")
        }

        if(password.length > 4){
            throw Exception("암호는 4자리 입니다.")
        }

        // TODO :  암호 문자 체킹 로직 필요.
    }

}
