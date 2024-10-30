package edu.example.learner_kotlin.member.dto.oauth2

interface OAuth2Response {
    fun getProvider(): String

    fun getProviderId(): String

    fun getEmail(): String

    fun getNickName(): String

    fun getPhoneNumber(): String

    fun getProfileImageUrl(): String
}

