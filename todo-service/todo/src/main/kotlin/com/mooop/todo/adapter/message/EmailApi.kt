package com.mooop.todo.adapter.message

import com.mooop.todo.application.shared.EmailSendRequest
import com.mooop.todo.application.shared.EmailSendResponse
import com.mooop.todo.application.shared.required.EmailSender
import org.springframework.stereotype.Component

@Component
class EmailApi : EmailSender {
    override fun send(emailSendRequest: EmailSendRequest): EmailSendResponse {
       // TODO : ...
        println("Send >> ${emailSendRequest}")
        return EmailSendResponse("SUCCESS" , "")
    }
}