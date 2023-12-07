package com.ykj.graduation_design.module.login.controller;

import cn.hutool.core.util.IdUtil;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.entity.User;
import com.ykj.graduation_design.common.utils.JwtUtil;
import com.ykj.graduation_design.module.login.DTO.LoginDto;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Resource
    JwtUtil jwtUtil;

    @PostMapping("login")
    public RestResult doLogin(@RequestBody LoginDto request) {
        try {

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LoginUser userDetails = (LoginUser) authentication.getPrincipal();
            String jwtToken = jwtUtil.getToken(userDetails.getUsername());
            log.info("请求成功，登录用户为：{}，token为：{}",request.getUserName(),jwtToken);
            return new RestResult(HttpStatus.OK, jwtToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info(request.getUserName(),request.getPassword());
            log.error("userName or password is not correct");
            return RestResult.FAIL;
        }
    }

    @PostMapping("register")
    public RestResult doRegister(@RequestBody User user) {
        try {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String password = passwordEncoder.encode(user.getPassword());
                user.setPassword(password);
                user.setId(Long.valueOf(IdUtil.getSnowflakeNextIdStr()));
                if (userService.insertUser(user) == 0) {
                    throw new Exception("用户名已存在");
                }
                String jwtToken = jwtUtil.getToken(user.getUserName());
                return new RestResult(HttpStatus.OK, jwtToken);
            } else
                throw new Exception("密码为空");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new RestResult(HttpStatus.EXPECTATION_FAILED, "注册失败" + e.getMessage());
        }
    }

}