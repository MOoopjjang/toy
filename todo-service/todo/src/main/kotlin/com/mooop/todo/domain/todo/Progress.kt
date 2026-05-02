package com.mooop.todo.domain.todo

import jakarta.persistence.Embeddable

@Embeddable
data class Progress( val progressValue:Int){
    fun isComplete(): Boolean = progressValue == 100
}
