package com.mooop.todo.adapter.security

import com.mooop.todo.adapter.security.config.AuthenticationPayload
import com.mooop.todo.adapter.security.dto.JwtCreateRequest
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.crypto.SecretKey
import kotlin.random.Random


class JwtService {
    companion object{
        val secretKey: SecretKey = Keys.hmacShaKeyFor("aaaaa8954oksfoig94ioasl0wlkelfgario4qffoqrjfjoif48ifoabbbbb".toByteArray())

        fun createToken(jwtCreateRequest: JwtCreateRequest):String{
            val claims: Claims = Jwts.claims().subject(jwtCreateRequest.payload.uid)
                .add("payload" , jwtCreateRequest.payload)
                .build()
            val now = toDate()
            val expire = now.time + (jwtCreateRequest.expireSec * 1000 )
            return Jwts.builder()
                .claims(claims)
                .header()
                .type("JWT")
                .and()
                .issuedAt(now)
                .expiration(Date(expire))
                .signWith(secretKey , Jwts.SIG.HS256)
                .compact()
        }

        fun validationCheck(token:String): Boolean{
            try{
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                return true
            }catch (e: Exception){
                e.printStackTrace()
                return false
            }
        }

        fun getTokenPayload(token:String): AuthenticationPayload?{
            if(!validationCheck(token)){
                return null
            }
            
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).let{
                val payload = it.payload["payload"]
                AuthenticationPayload(
                    (payload as Map<*, *>)["role"] as String,
                    payload["username"] as String,
                    payload["userid"] as String,
                    payload["uid"] as String
                )
            }
        }

        private fun toDate() : Date {
            return LocalDateTime.now().let {
                val inst = it.atZone(ZoneId.systemDefault()).toInstant()
                Date.from(inst)
            }
        }
    }
}


fun main(args:Array<String>){
    val payload = AuthenticationPayload(
        "ADMIN", "cwkim", "xferlog", Random(16).toString()
    )
//    val secretKey = Keys.hmacShaKeyFor("aaaaa8954oksfoig94ioasl0wlkelfgario4qffoqrjfjoif48ifoabbbbb".toByteArray())
    val jwtCreateRequest = JwtCreateRequest(
        3600, payload
    )

    val token = JwtService.createToken(jwtCreateRequest)
    println("token = ${token}")

    println("##############################################")
    val validate = JwtService.validationCheck(token)
    println("validate = ${validate}")

    println("##############################################")
    val info = JwtService.getTokenPayload(token)
    println("info = ${info}")

}
