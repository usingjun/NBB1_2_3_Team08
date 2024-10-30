package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.exception.MemberException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class CustomUserPrincipal(member: Member) : UserDetails {
    private val username: String = member.email ?: throw MemberException.MEMBER_NOT_FOUND.memberTaskException
    private val role: String = member.role.toString()


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_" + this.role))
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return username
    }
}
