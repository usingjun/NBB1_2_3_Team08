package edu.example.learner_kotlin.member.dto

import edu.example.learner_kotlin.member.entity.Member
import jakarta.servlet.http.Cookie


data class LoginDTO(
    var email: String,
    var password: String,
    var cookie: Cookie? = null,
    var memberId: Long? = null
) {
    constructor(email: String, password: String) : this(email, password, null, null)
}

