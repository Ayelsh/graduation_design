package com.ykj.graduation_design.module.login.controller;

import cn.hutool.core.util.IdUtil;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.entity.User;
import com.ykj.graduation_design.common.utils.AccessAddressUtils;
import com.ykj.graduation_design.common.utils.JWTTokenUtils;
import com.ykj.graduation_design.config.JWTConfig;
import com.ykj.graduation_design.module.login.DTO.LoginDto;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
@Slf4j
public class UserController {

    @Resource
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Resource
    PasswordEncoder passwordEncoder;


    @PostMapping("login")
    public void doLogin(@Validated @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String ip = AccessAddressUtils.getIpAddress(request);
            LoginUser userDetails = (LoginUser) authentication.getPrincipal();
            userDetails.setIp(ip);
            String jwtToken = JWTTokenUtils.createAccessToken(userDetails);

            JWTTokenUtils.setTokenInfo(jwtToken, userDetails.getUsername(), ip);

            log.info("请求成功，登录用户为：{}，token为：{}，已保存到redis", loginDto.getUserName(), jwtToken);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", jwtToken);
            RestResult.responseJson(response, new RestResult<>(200, "登录成功", tokenMap));

        } catch (Exception e) {

            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(406, "登录失败", "账号或密码错误，请重试！"));

        }
    }

    @PostMapping("register")
    public void doRegister(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String password = passwordEncoder.encode(user.getPassword());
                user.setPassword(password);
                user.setId(Long.valueOf(IdUtil.getSnowflakeNextIdStr()));
                if (userService.insertUser(user) == 0) {
                    throw new Exception("用户名已存在");
                }

//                String jwtToken = JwtUtil.getToken(new LoginUser(user, AccessAddressUtils.getIpAddress(request)));
                RestResult.responseJson(response, new RestResult<>(200, "注册成功", "请于登录页面登录"));
            } else
                throw new Exception("密码为空");
        } catch (Exception e) {
            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(417, "注册失败", "e.getMessage()"));

        }
    }

    @DeleteMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 添加到黑名单
        String token = request.getHeader(JWTConfig.tokenHeader);
        JWTTokenUtils.addBlackList(token);

        log.info("用户{}登出成功，Token信息已保存到Redis的黑名单中", JWTTokenUtils.getUserNameByToken(token));

        SecurityContextHolder.clearContext();
        RestResult.responseJson(response, new RestResult<>(200, "登出成功", null));
    }
}
