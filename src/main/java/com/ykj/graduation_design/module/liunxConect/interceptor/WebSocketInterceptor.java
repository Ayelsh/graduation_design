package com.ykj.graduation_design.module.liunxConect.interceptor;
import cn.hutool.core.lang.UUID;
import com.ykj.graduation_design.config.ConstantPool;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/05/17:33
 * @Description: 连接拦截器
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("进入websocket拦截器");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {

            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;

            LoginUser userDetails =
                    (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //这个map存在WebSocketSession
            map.put(ConstantPool.USERNAME, userDetails.getUsername());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}