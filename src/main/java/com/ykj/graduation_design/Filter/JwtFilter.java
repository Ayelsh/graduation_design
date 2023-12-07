package com.ykj.graduation_design.Filter;

import com.ykj.graduation_design.common.utils.JwtUtil;
import com.ykj.graduation_design.module.login.service.UserService;
import com.ykj.graduation_design.module.login.service.impl.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
 
    @Resource
    JwtUtil jwtUtil;
 
    @Autowired
    UserDetailServiceImpl userDetailsService;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String jwtToken = request.getHeader("token");//从请求头中获取token
        if (jwtToken != null && !jwtToken.isEmpty() && jwtUtil.checkToken(jwtToken)){
            try {//token可用
                Claims claims = jwtUtil.getTokenBody(jwtToken);
                String userName = (String) claims.get("userName");
                UserDetails user = userDetailsService.loadUserByUsername(userName);
                if (user != null){
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }else {
            log.warn("token is null or empty or out of time, probably user is not log in !");
        }
        filterChain.doFilter(request, response);//继续过滤
    }
}