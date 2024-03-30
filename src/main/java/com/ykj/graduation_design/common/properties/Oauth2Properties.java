package com.ykj.graduation_design.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "github")
public class Oauth2Properties {
    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
    private String accessTokenUrl;
    private String userInfoUrl;

    public String generateCodeUrl(String code) {
        return accessTokenUrl +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&grant_type=authorization_code";
    }


}
