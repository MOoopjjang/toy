package com.mooop.todo.adapter.webapi.member

import com.mooop.todo.adapter.security.MemberAuthenticationProvider
import com.mooop.todo.adapter.shared.UpDnUtil
import com.mooop.todo.adapter.webapi.member.dto.MemberInfo
import com.mooop.todo.adapter.webapi.member.dto.PhotoUploadResponse
import com.mooop.todo.adapter.webapi.presentation.code.TodoError
import com.mooop.todo.application.member.provided.MemberFindRequest
import com.mooop.todo.application.member.provided.MemberFinder
import com.mooop.todo.application.member.provided.MemberRegister
import com.mooop.todo.domain.member.Member
import com.mooop.todo.domain.member.MemberRegisterRequest
import com.mooop.todo.domain.member.MemberStatus
import com.mooop.todo.domain.member.MemberUpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class MemberService constructor(
    private val memberRegister: MemberRegister,
    private val memberFinder: MemberFinder
    ,private val memberAuthenticationProvider: MemberAuthenticationProvider
){

    private val log = LoggerFactory.getLogger(MemberService::class.java)


    @Value("\${todo.upload.path}")
    private lateinit var uploadPath:String

    fun registerMemberInfo(memberRegisterRequest: MemberRegisterRequest , photo: MultipartFile?): MemberInfo{
        /** Photo upload */
        photo?.let {
            val uploadResultInfo = UpDnUtil.upload(photo , uploadPath.plus("/photo"))
            memberRegisterRequest.profileImage = createPhotoUrl(uploadResultInfo.second)
        }
        log.info(">>>> memberRegisterRequest.profileImage = {}",memberRegisterRequest.profileImage)
        return memberRegister.register(memberRegisterRequest).let {MemberInfo.of(it)}
    }

    fun updateMemberInfo(memberUpdateRequest: MemberUpdateRequest , photo: MultipartFile?): MemberInfo{

        /** Photo upload */
        photo?.let {
            val uploadResultInfo = UpDnUtil.upload(photo , uploadPath.plus("/photo"))
            memberUpdateRequest.profileImage = createPhotoUrl(uploadResultInfo.second)
        }

        val memberInfo = getMember()
        return memberRegister.updateInfo(memberInfo.id() , memberUpdateRequest).let {
            MemberInfo.of(it)
        }
    }

    fun changeMemberStatusToActivate(memberId:Long): MemberInfo{
        return memberRegister.activate(memberId).let {
            MemberInfo.of(it)
        }
    }

    fun changeMemberStatusToWithdraw(memberId:Long): MemberInfo{
        return memberRegister.withdraw(memberId).let {
            MemberInfo.of(it)
        }


    }

    fun getMemberInfoFromUserId(): MemberInfo? = MemberInfo.of(getMember())


    fun getMemberInfoFromMemberId(): MemberInfo?{
        val memberInfo = getMember()
        return memberFinder.getMember(memberInfo.id())?.let {
            MemberInfo.of(it)
        }


    }

    fun getMemberInfoFromStatus(status:String):List<MemberInfo>{
        return memberFinder.findMembers(MemberFindRequest(MemberStatus.valueOf(status))).let { list->
            if(list.isEmpty()){
                listOf()
            }else{
                list.map { MemberInfo.of(it) }
            }
        }
    }



    fun procPhotoUpload(mf: MultipartFile): PhotoUploadResponse {
        val memberInfo = getMember()
        val uploadResultInfo = UpDnUtil.upload(mf , uploadPath.plus("/photo"))
        val uri = createPhotoUrl(uploadResultInfo.second)

        return memberRegister.updateInfo(memberInfo.id()  , MemberUpdateRequest(profileImage = uri)).let {
            PhotoUploadResponse(uploadResultInfo.first , uploadResultInfo.second , uri)
        }
    }

    private fun createPhotoUrl(photoName:String):String = "/res/photo/".plus(photoName)


    private fun getMember(): Member{
        val authInfo = memberAuthenticationProvider.getAuthenticationInfo()?:throw Exception(TodoError.E001.message)
        return memberFinder.getMember(authInfo.userid)?:throw Exception(TodoError.E001.message)
    }



}
