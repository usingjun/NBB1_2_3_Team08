package edu.example.learner_kotlin.member.dto

import edu.example.learner_kotlin.member.entity.Member
import jakarta.servlet.http.Cookie


data class LoginDTO(
    var email: String? = null,
    var password: String? = null,
    var cookie: Cookie? = null,
    var memberId: Long? = null
) {
    constructor(email: String?, password: String?) : this(email, password, null, null)

    constructor(cookie: Cookie?, memberId: Long?) : this(null,null, cookie, memberId)
}

