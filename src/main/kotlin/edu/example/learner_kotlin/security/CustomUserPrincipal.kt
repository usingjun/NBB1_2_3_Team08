package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.exception.MemberException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class CustomUserPrincipal(
    private val member : Member
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_" + this.member.role))
    }

    override fun getPassword(): String? {
        return member.password
    }

    override fun getUsername(): String? {
        return member.nickname
    }

    fun getMemberId(): Long? {
        return member.memberId
    }

}
