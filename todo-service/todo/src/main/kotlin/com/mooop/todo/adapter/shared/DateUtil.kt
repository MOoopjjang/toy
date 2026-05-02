package com.mooop.todo.adapter.shared

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    fun convertLDTToString(ldt:LocalDateTime , fmt:String? = null):String{
        val tmp = fmt?:"yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(tmp)
        return ldt.format(formatter)
    }

    fun convertStringToLD(date:String): LocalDate =
        DateTimeFormatter.ofPattern("yyyy-MM-dd").let {
            LocalDate.parse(date , it)
        }
}