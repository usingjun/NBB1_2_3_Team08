package edu.example.learner.member.dto.Oauth2;

import lombok.Data;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map <String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getNickName() {
        return attribute.get("nickname").toString();
    }

    @Override
    public String getPhoneNumber() {
        return attribute.get("mobile").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attribute.get("profileImageUrl").toString();
    }
}
