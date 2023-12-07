package com.ykj.graduation_design.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gitee")
public class Oauth2FromGiteeProperties {

    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
    private String accessTokenUrl;
    private String userInfoUrl;

    public String generateCodeUrl(String code) {
        return accessTokenUrl +
                "?grant_type=authorization_code" +
                "&code=" + code + "?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&client_secret=" + clientSecret;
    }

//    https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=

}
