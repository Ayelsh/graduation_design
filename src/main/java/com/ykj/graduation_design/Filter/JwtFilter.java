package com.ykj.graduation_design.Filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.utils.AccessAddressUtils;
import com.ykj.graduation_design.common.utils.JWTTokenUtils;
import com.ykj.graduation_design.config.JWTConfig;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        //从请求头中获取token
        String token = request.getHeader("token");

        //是否是系统定义的TOKEN
        if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
            // 是否在黑名单中
            if (JWTTokenUtils.isBlackList(token)) {
                RestResult.responseJson(response, new RestResult<>(505, "Token已失效", "Token已进入黑名单"));
                return;
            }

            //redis存在判断
            if (JWTTokenUtils.hasToken(token)) {

                String ip = AccessAddressUtils.getIpAddress(request);
                String expiration = JWTTokenUtils.getExpirationByToken(token);
                String username = JWTTokenUtils.getUserNameByToken(token);

                //过期判断
                if (JWTTokenUtils.isExpiration(expiration)) {
                    // 加入黑名单
                    JWTTokenUtils.addBlackList(token);
                    // 是否在刷新期内
                    String validTime = JWTTokenUtils.getRefreshTimeByToken(token);
                    if (JWTTokenUtils.isValid(validTime)) {
                        // 刷新Token，重新存入请求头
                        String newToke = JWTTokenUtils.refreshAccessToken(token);

                        // 删除旧的Token，并保存新的Token
                        JWTTokenUtils.deleteRedisToken(token);
                        JWTTokenUtils.setTokenInfo(newToke, username, ip);
                        response.setHeader(JWTConfig.tokenHeader, newToke);

                        log.info("用户{}的Token已过期，但未超过刷新时间，已刷新", username);

                        token = newToke;

                    } else {

                        log.info("用户{}的Token已过期且超过刷新时间，不予刷新", username);

                        // 加入黑名单
                        JWTTokenUtils.addBlackList(token);
                        RestResult.responseJson(response, new RestResult<>(505, "Token已过期", "已超过刷新有效期"));
                        return;
                    }


                }

                //解析获取tokenUserData
                LoginUser tokenUser = JWTTokenUtils.parseAccessToken(token);

                if (tokenUser != null) {
                    // 校验IP
                    if (!StringUtils.equals(ip, tokenUser.getIp())) {

                        log.info("用户{}请求IP:{}与Token中IP信息:{}不一致", username,ip,tokenUser.getIp());

                        // 加入黑名单
                        JWTTokenUtils.addBlackList(token);
                        RestResult.responseJson(response, new RestResult<>(505, "Token已过期", "可能存在IP伪造风险"));
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            tokenUser, tokenUser.getUser().getId(), tokenUser.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }


            }
            else {
                log.info("Token不存在");
                JWTTokenUtils.addBlackList(token);
                RestResult.responseJson(response, new RestResult<>(505, "Token无效", "Token不存在"));
                return;
            }
        }
        filterChain.doFilter(request, response);//继续过滤
    }
}