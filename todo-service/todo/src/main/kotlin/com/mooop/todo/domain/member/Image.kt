package com.mooop.todo.domain.member

import jakarta.persistence.Embeddable


@Embeddable
data class Image(val imageUrl:String){

    fun exist():Boolean{
        // TODO : ...
        return true
    }
}
