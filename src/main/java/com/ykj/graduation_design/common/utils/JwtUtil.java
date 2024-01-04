package com.ykj.graduation_design.common.utils;

import com.ykj.graduation_design.module.login.entity.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
 
    //常量
    public static final long EXPIRE = 1000 * 60 * 60 * 4; //token过期时间,4个小时
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO"; //秘钥
 
    //生成token字符串的方法
    public static String getToken(LoginUser user){
        return Jwts.builder()
                .setId(String.valueOf(user.getUser().getId()))
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("userName", user.getUsername())//设置token主体部分 ，存储用户信息
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
    }

//    String token = Jwts.builder()// 设置JWT
//            .setId(sysUserDetails.getId().toString()) // 用户Id
//            .setSubject(sysUserDetails.getUsername()) // 主题
//            .setIssuedAt(new Date()) // 签发时间
//            .setIssuer("C3Stones") // 签发者
//            .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration)) // 过期时间
//            .signWith(SignatureAlgorithm.HS512, JWTConfig.secret) // 签名算法、密钥
//            .claim("authorities", JSON.toJSONString(sysUserDetails.getAuthorities()))// 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
//            .claim("ip", sysUserDetails.getIp()).compact();
//		return JWTConfig.tokenPrefix + token;
 
    //验证token字符串是否是有效的  包括验证是否过期
    public boolean checkToken(String jwtToken) {
        if(jwtToken == null || jwtToken.isEmpty()){
            log.error("Jwt is empty");
            return false;
        }
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            Claims body = claims.getBody();
            if ( body.getExpiration().after(new Date(System.currentTimeMillis()))){
                return true;
            } else
                return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
 
    public Claims getTokenBody(String jwtToken){
        if(jwtToken == null || jwtToken.isEmpty()){
            log.error("Jwt is empty");
            return null;
        }
        try {
            return Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken).getBody();
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}