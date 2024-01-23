package com.ykj.graduation_design.Filter;

import cn.hutool.core.util.StrUtil;
import com.ykj.graduation_design.common.entity.HeaderMapRequestWrapper;
import com.ykj.graduation_design.config.JWTConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class RequestReplaceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        log.info("进入token前置过滤器检查!");
//        String contentType = request.getContentType();
        request = this.addTokenForWebSocket(request, response);
        filterChain.doFilter(request, response);
    }

    private HttpServletRequest addTokenForWebSocket(HttpServletRequest request, HttpServletResponse response) {
        ;
        String token = request.getHeader("authorization");
        if (StrUtil.isNotBlank(token)) {
            return request;
        }
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        token = request.getHeader("Sec-WebSocket-Protocol");
        if (StrUtil.isBlank(token)) {
            return request;
        }
        requestWrapper.addHeader("token", JWTConfig.tokenPrefix + token);
        response.addHeader("Sec-WebSocket-Protocol", token);
        log.info("增加token:{}",token);
        response.addHeader("Access-Contro1-Allow-Origin", "*");
        return requestWrapper;
    }
}

