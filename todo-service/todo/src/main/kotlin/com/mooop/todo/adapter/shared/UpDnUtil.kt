package com.mooop.todo.adapter.shared

import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import kotlin.io.path.Path

object UpDnUtil {
    fun upload(mf: MultipartFile , path:String): Pair<String , String>{
        return mf.originalFilename?.let {
            val fileName = createFileName(it)
            val uploadPath = path.plus("/").plus(fileName)
            println("uploadPath = ${uploadPath}")
            // 파일 전송 ( upload )
            mf.transferTo(Path(uploadPath))

            Pair(it , fileName)
        }?:throw Exception(TodoError.E005.message)
    }

    private fun createFileName(originalName:String):String{
        val p1 = System.currentTimeMillis().toString()
        val ext = originalName.split("\\.").let { it.get(it.size-1)}
        println("ext = ${ext}")
        return UUID.randomUUID().toString().replace("-","").let {
            p1.plus(it).plus(".").plus(ext)
        }
    }


}