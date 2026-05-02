package com.mooop.todo.domain.todo

import jakarta.persistence.Embeddable

@Embeddable
data class Priority(val priorityValue:Int){
    init{
        if((priorityValue < -1) || (priorityValue > 99)){
            throw Exception("우선순위 범위는 -1 ~ 99 까지 입니다.")
        }
    }
}
