package edu.example.learner.member.dto.Oauth2;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getNickName();

    String getPhoneNumber();

    String getProfileImageUrl();
}
