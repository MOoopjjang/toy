package com.mooop.todo.domain.todo

enum class TodoCategory {
    STUDY,
    HEALTH,
    HOBBY,

    ETC
    ;


//    fun toEnum(category:String): TodoCategory{
//        return TodoCategory.entries.first { it.name == category }
//    }

    companion object{
        fun toEnum(category:String): TodoCategory{
            return TodoCategory.entries.first { it.name == category }
        }
    }
}