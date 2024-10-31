package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.exception.MemberException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class CustomUserPrincipal(
    private val username: String,
    private val role: String
): UserDetails {
    constructor(member : Member) : this(
        username = member.nickname.toString(),
        role = member.role.toString()
    )

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
