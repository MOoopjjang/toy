package com.mooop.todo.adapter.webapi.presentation.code

enum class TodoError constructor(
    val message:String
){
    E000("인증정보가 존재하지 않습니다."),
    E001("회원정보를 찾을수 없습니다."),
    E002("업데이트에 실패하였습니다."),
    E003("아이디/패스워드를 확인해주세요."),
    E004("승인대기중 입니다.."),
    E005("파일업로드에 실패하였습니다."),
    E006("content가 존재하지 않습니다.")

}