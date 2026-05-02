package com.mooop.todo.application.shared.required

import com.mooop.todo.application.shared.EmailSendRequest
import com.mooop.todo.application.shared.EmailSendResponse

interface EmailSender {

    fun send(emailSendRequest: EmailSendRequest): EmailSendResponse
}