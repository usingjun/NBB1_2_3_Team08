package edu.example.learner_kotlin.member.dto.oauth2

class GoogleResponse(private val attribute: Map<String, Any>) : OAuth2Response {
    override fun getProvider(): String {
        return "google"
    }

    override fun getProviderId(): String {
        return attribute["sub"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getNickName(): String {
        return attribute["name"].toString()
    }

    override fun getPhoneNumber(): String {
        return ""
    }

    override fun getProfileImageUrl(): String {
        return attribute["picture"].toString()
    }
}
