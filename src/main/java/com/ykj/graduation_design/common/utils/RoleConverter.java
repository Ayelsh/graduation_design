package com.ykj.graduation_design.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author Ayelsh.ye
 */
@Component
public class RoleConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static Set<GrantedAuthority> convertRoles(String rolesString) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Iterable<String> roles = Splitter.on(":").split(rolesString);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public static Set<GrantedAuthority> convertJsonToRoles(String jsonRoles) throws IOException {
        Set<GrantedAuthority> authorities = new HashSet<>();

        List<Map<String, String>> roleJsonList = objectMapper.readValue(jsonRoles, new TypeReference<List<Map<String, String>>>() {});
        for (Map<String, String> roleJson : roleJsonList) {
            String authority = roleJson.get("authority");
            String[] roles = authority.split(",\\s*");
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.trim()));
            }
        }
        return authorities;
    }


}
