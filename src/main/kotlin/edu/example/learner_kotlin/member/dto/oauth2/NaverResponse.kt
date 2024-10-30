package edu.example.learner_kotlin.member.dto.oauth2

class NaverResponse(attribute: Map<String, Any>) : OAuth2Response {

    private val attribute = attribute["response"] as Map<String, Any>

    override fun getProvider(): String {
        return "naver"
    }

    override fun getProviderId(): String {
        return attribute["id"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getNickName(): String {
        return attribute["nickname"].toString()
    }

    override fun getPhoneNumber(): String {
        return attribute["mobile"].toString()
    }

    override fun getProfileImageUrl(): String {
        return attribute["profileImageUrl"].toString()
    }
}
