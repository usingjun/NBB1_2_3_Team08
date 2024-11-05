package edu.example.learner_kotlin.member.dto.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User


class CustomOauth2User(
    private val oauth2Response: OAuth2Response,
    private val role: String,
    val memberId: Long?
) : OAuth2User {
    override fun getName(): String {
        return oauth2Response.getNickName()
    }

    override fun getAttributes(): MutableMap<String?, Any?> {
        return mutableMapOf("category" to "refresh", "username" to oauth2Response.getNickName(), "role" to "ROLE_$role", "mid" to memberId)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_$role"))
    }
}
