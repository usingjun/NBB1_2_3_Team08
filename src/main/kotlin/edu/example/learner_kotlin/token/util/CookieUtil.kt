package edu.example.learner_kotlin.token.util

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component

@Component
class CookieUtil {
    fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60
        cookie.secure = false;
        cookie.path = "/";
        cookie.isHttpOnly = true

        return cookie
    }
}

