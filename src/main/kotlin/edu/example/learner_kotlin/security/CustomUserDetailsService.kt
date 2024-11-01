package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.exception.MemberException
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        val memberData = memberRepository.getMemberByEmail(username) ?:
        throw MemberException.MEMBER_NOT_FOUND.memberTaskException


        return CustomUserPrincipal(memberData)
    }
}
