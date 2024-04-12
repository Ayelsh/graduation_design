package com.ykj.graduation_design.common.utils;


import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.config.JWTConfig;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class JWTTokenUtils {


    /**
     * 时间格式化
     */
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserDetailsService userDetailsService;

    private static JWTTokenUtils jwtTokenUtils;

    @PostConstruct
    public void init() {
        jwtTokenUtils = this;
        jwtTokenUtils.userDetailsService = this.userDetailsService;
    }

    /**
     * 创建Token
     *
     * @param loginUser 用户信息
     * @return
     */
    public static String createAccessToken(LoginUser loginUser) {

        String token = Jwts.builder()// 设置JWT
                .setId(loginUser.getSysUser().getId().toString()) // 用户Id
                .setSubject(loginUser.getUsername()) // 主题
                .setIssuedAt(new Date()) // 签发时间
                .setIssuer("Ayelsh") // 签发者
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, JWTConfig.secret) // 签名算法、密钥
                .claim("authorities", JSON.toJSONString(loginUser.getAuthorities())) // 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
                .claim("ip", loginUser.getIp())
                .compact();//以String格式输出
        //
        return JWTConfig.tokenPrefix +token;
    }

    /**
     * 解析Token
     *
     * @param token Token信息
     * @return
     */
    public static LoginUser parseAccessToken(String token) {
        LoginUser loginUser = null;
        if (StringUtils.isNotEmpty(token)) {
            try {
                // 去除JWT前缀
                token = token.substring(JWTConfig.tokenPrefix.length());

                // 解析Token
                Claims claims = Jwts.parser().setSigningKey(JWTConfig.secret).parseClaimsJws(token).getBody();

                // 获取用户信息
                loginUser = new LoginUser(new SysUser(),"");
                loginUser.setId(Long.parseLong(claims.getId()));

                loginUser.setUsername(claims.getSubject());
                loginUser.setIp((String) claims.get("ip"));
//                 获取角色
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                String authority = claims.get("authorities").toString();
                if (StringUtils.isNotEmpty(authority)) {
                     authorities = RoleConverter.convertJsonToRoles(authority);
//                            JSON.parseObject(authority, new TypeReference<List<Map<String, String>>>() {});
//                    for (Map<String, String> role : authorityList) {
//                        if (!role.isEmpty()) {
//                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
//                        }
//                    }
                }
				loginUser.setAuthorities(authorities);
            } catch (Exception e) {
                log.error("解析Token异常：" + e);
            }
        }
        return loginUser;
    }


    /**
     * 刷新Token
     *
     * @param oldToken 过期但未超过刷新时间的Token
     * @return
     */
    public static String refreshAccessToken(String oldToken) {
        String username = JWTTokenUtils.getUserNameByToken(oldToken);
        LoginUser loginUser = (LoginUser) jwtTokenUtils.userDetailsService
                .loadUserByUsername(username);
        loginUser.setIp(JWTTokenUtils.getIpByToken(oldToken));
        return createAccessToken(loginUser);
    }


    /**
     * 保存Token信息到Redis中
     *
     * @param token    Token信息
     * @param username 用户名
     * @param ip       IP
     */
    public static void setTokenInfo(String token, String username, String ip) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());

            Integer refreshTime = JWTConfig.refreshTime;
            LocalDateTime localDateTime = LocalDateTime.now();

            RedisUtils.hset(token, "username", username, refreshTime);
            RedisUtils.hset(token, "ip", ip, refreshTime);
            RedisUtils.hset(token, "refreshTime",
                    df.format(localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS)), refreshTime);
            RedisUtils.hset(token, "expiration", df.format(localDateTime.plus(JWTConfig.expiration, ChronoUnit.MILLIS)),
                    refreshTime);
        }
    }

    /**
     * 将Token放到黑名单中
     *
     * @param token Token信息
     */
    public static void addBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            RedisUtils.hset("blackList", token, df.format(LocalDateTime.now()));
        }
    }

    /**
     * Redis中删除Token
     *
     * @param token Token信息
     */
    public static void deleteRedisToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            RedisUtils.deleteKey(token);
        }
    }

    /**
     * 判断当前Token是否在黑名单中
     *
     * @param token Token信息
     */
    public static boolean isBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hasKey("blackList", token);
        }
        return false;
    }

    /**
     * 是否过期
     *
     * @param expiration 过期时间，字符串
     * @return 过期返回True，未过期返回false
     */
    public static boolean isExpiration(String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.isAfter(expirationTime)) {
            return true;
        }
        return false;
    }

    /**
     * 是否有效
     *
     * @param refreshTime 刷新时间，字符串
     * @return 有效返回True，无效返回false
     */
    public static boolean isValid(String refreshTime) {
        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(validTime) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 检查Redis中是否存在Token
     *
     * @param token Token信息
     * @return
     */
    public static boolean hasToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hasKey(token);
        }
        return false;
    }

    /**
     * 从Redis中获取过期时间
     *
     * @param token Token信息
     * @return 过期时间，字符串
     */
    public static String getExpirationByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "expiration").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取刷新时间
     *
     * @param token Token信息
     * @return 刷新时间，字符串
     */
    public static String getRefreshTimeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "refreshTime").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取用户名
     *
     * @param token Token信息
     * @return
     */
    public static String getUserNameByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "username").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取IP
     *
     * @param token Token信息
     * @return
     */
    public static String getIpByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "ip").toString();
        }
        return null;
    }

}