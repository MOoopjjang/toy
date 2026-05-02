package com.mooop.todo.adapter.webapi.presentation

import com.mooop.todo.adapter.security.MemberAuthenticationProvider
import com.mooop.todo.adapter.webapi.RestApiResponse
import com.mooop.todo.adapter.webapi.member.MemberService
import com.mooop.todo.adapter.webapi.member.dto.MemberInfo
import com.mooop.todo.adapter.webapi.member.dto.PhotoUploadResponse
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import com.mooop.todo.application.member.provided.MemberRegister
import com.mooop.todo.domain.member.MemberRegisterRequest
import com.mooop.todo.domain.member.MemberUpdateRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value =["/member"])
class TodoMemberComponentApi constructor(
    val memberRegister: MemberRegister
    ,private val memberService: MemberService
    ,private val memberAuthenticationProvider: MemberAuthenticationProvider
) {

    @PostMapping("/register" , consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun register(@RequestPart(name = "memberRegisterRequest") memberRegisterRequest: MemberRegisterRequest
    ,@RequestPart(name = "photo" , required = false) photo: MultipartFile?
    ) : RestApiResponse<MemberInfo> {
        return try{
            memberService.registerMemberInfo(memberRegisterRequest , photo).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }

    @GetMapping("/activate/{memberId}")
    fun activate(@PathVariable memberId:Long): RestApiResponse<MemberInfo> {
        return try{
            memberService.changeMemberStatusToActivate(memberId).let {
                RestApiResponse.Companion.ok(it)
            }
        }catch (e:Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @PostMapping("/update", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun update(@RequestPart(name = "memberUpdateRequest") memberUpdateRequest: MemberUpdateRequest
               ,@RequestPart(name = "photo" , required = false) photo: MultipartFile?): RestApiResponse<MemberInfo> {
        return try{
            memberService.updateMemberInfo( memberUpdateRequest,photo).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @PostMapping("/withdraw/{memberId}")
    fun withdraw(@PathVariable memberId:Long): RestApiResponse<MemberInfo> {
        return try{
            memberService.changeMemberStatusToWithdraw(memberId).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @GetMapping("/info/status")
    fun informationFromStatus(@RequestParam(name = "status") status:String): RestApiResponse<List<MemberInfo>> {
        return try{
            memberService.getMemberInfoFromStatus(status).let {
                RestApiResponse.ok(it)
            }
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }


    @GetMapping("/info")
    fun getMemberInfo(): RestApiResponse<MemberInfo>{
        return try{
            val authenticationInfo = memberAuthenticationProvider.getAuthenticationInfo()!!
            memberService.getMemberInfoFromUserId()?.let {
                RestApiResponse.ok(it)
            }?:RestApiResponse.fail(TodoError.E001.message)
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }
    }



    @PostMapping("/upload")
    fun uploadPhoto( @RequestParam("file") file: MultipartFile): RestApiResponse<PhotoUploadResponse>{
        return try{
            RestApiResponse.ok(memberService.procPhotoUpload( file))
        }catch (e: Exception){
            e.printStackTrace()
            RestApiResponse.fail(e.message)
        }

    }

}