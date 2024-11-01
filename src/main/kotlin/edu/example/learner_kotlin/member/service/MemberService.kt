package edu.example.learner_kotlin.member.service

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.LoginDTO
import edu.example.learner_kotlin.member.dto.MemberDTO
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.exception.LoginException
import edu.example.learner_kotlin.member.exception.MemberException
import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.security.JWTUtil
import jakarta.servlet.http.Cookie
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val modelMapper: ModelMapper,
    private val jwtUtil: JWTUtil
){
    //회원가입
    fun register(memberDTO: MemberDTO): MemberDTO {
        //존재하는 이메일인지 확인
        memberRepository.getMemberByEmail(memberDTO.email) ?:
            throw MemberException.EMAIL_ALREADY_EXISTS.memberTaskException

        //존재하는 닉네임인지 확인
        memberRepository.getMemberByNickName(memberDTO.nickname) ?:
            throw MemberException.NICKNAME_ALREADY_EXISTS.memberTaskException


        try {
            val member = modelMapper.map(memberDTO, Member::class.java)
            val saveMember = memberRepository.save(member)
            return MemberDTO(saveMember)
        } catch (e: Exception) {
            log.error("회원가입 도중 오류 발생", e)
            throw MemberException.MEMBER_NOT_REGISTERED.memberTaskException
        }
    }

    //사진 업로드 및 수정
    fun uploadImage(file: MultipartFile, memberId: Long?): MemberDTO {
        try {
            val member: Member = memberRepository.getMemberInfo(memberId)
                ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException
            member.apply{
                profileImage = file.bytes
                imageType = file.contentType
            }
            log.info("change profile image")
            return MemberDTO(memberRepository.save(member))
        } catch (e: Exception) {
            throw MemberException.NOT_UPLOAD_IMAGE.memberTaskException
        }
    }

    //사진 삭제
    fun removeImage(memberId: Long?) {
        val member: Member = memberRepository.findByIdOrNull(memberId)
            ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException
        try {
            // 프로필 이미지를 null로 설정합니다.
            member.profileImage = null

            memberRepository.save(member)
            // 변경된 회원 정보를 저장합니다.
        } catch (e: Exception) {
            throw MemberException.NOT_REMOVE_IMAGE.memberTaskException
        }
    }

    //회원 정보 조회
    fun getMemberInfo(memberId: Long?): MemberDTO {
        return run{
            val member: Member = memberRepository.findByIdOrNull(memberId)
                ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException
            MemberDTO(member)
        }
    }

    //다른 회원 정보 조회
    fun getMemberInfoNickName(nickname: String?): MemberDTO {
        return run{
            val member: Member = memberRepository.getMemberByNickName(nickname) ?:
                throw MemberException.MEMBER_NOT_FOUND.memberTaskException

           MemberDTO(member).otherMember()
        }
    }

    //회원 정보 수정
    fun updateMemberInfo(memberId: Long?, memberDTO: MemberDTO): MemberDTO {
        val member: Member = memberRepository.findByIdOrNull(memberId)
            ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException

        //닉네임 수정
        try {
            member.nickname = memberDTO.nickname
        } catch (e: Exception) {
            throw MemberException.NICKNAME_ALREADY_EXISTS.memberTaskException
        }

        //자기소개 및 비밀번호 수정
        try {
            member.apply{
                introduction = memberDTO.introduction
                password = passwordEncoder.encode(memberDTO.password)
            }
            return  MemberDTO(memberRepository.save(member))
        } catch (e: Exception) {
            throw MemberException.MEMBER_NOT_MODIFIED.memberTaskException
        }
    }

    //회원 탈퇴
    fun deleteMember(memberId: Long?) {
        try {
            memberRepository.delete(memberRepository.findByIdOrNull(memberId)
                ?:throw MemberException.MEMBER_NOT_FOUND.memberTaskException)
        } catch (e: Exception) {
            throw MemberException.MEMBER_NOT_DELETE.memberTaskException
        }
    }

    //이메일로 닉네임 얻기
    fun getNicknameByEmail(email: String?): String {
        return run{
            memberRepository.findByEmail(email).nickname ?:
                throw MemberException.MEMBER_NOT_FOUND.memberTaskException
        }
    }

    //비밀번호 인증
    fun verifyPassword(memberId: Long?, rawPassword: String): Boolean {
        return run{
            val member: Member = memberRepository.findByIdOrNull(memberId)
                ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException
            log.info("인증 비밀번호 $rawPassword")
            log.info("본래 비밀번호 " + member.password)
            // 저장된 비밀번호와 입력된 비밀번호를 비교합니다.
            passwordEncoder.matches(rawPassword, member.password)
        }
    }

    //전체 회원 조회
    fun allMembers(): MutableList<MemberDTO> {
        val members: List<Member> = memberRepository.findAll()

        val memberDTOS: MutableList<MemberDTO> = ArrayList<MemberDTO>()
        for (member in members) {
            val memberDTO: MemberDTO = MemberDTO(member)
            memberDTOS.add(memberDTO)
        }
        return memberDTOS
    }
}