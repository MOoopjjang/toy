package com.mooop.todo.adapter.shared

import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils

object HttpUtil {

    fun extractHeaderValue(request: HttpServletRequest, key:String):String = StringUtils.defaultIfEmpty(request.getHeader(key) , "")
}