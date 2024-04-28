package com.ykj.graduation_design.module.login.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.common.entity.UserInfo;
import com.ykj.graduation_design.common.utils.AccessAddressUtils;
import com.ykj.graduation_design.common.utils.JWTTokenUtils;
import com.ykj.graduation_design.common.utils.UserUtils;
import com.ykj.graduation_design.config.JWTConfig;
import com.ykj.graduation_design.module.login.DTO.LoginDto;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.service.EmailService;
import com.ykj.graduation_design.module.login.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ayelsh.ye
 */
@RestController
@RequestMapping("/user/")
@Slf4j
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Resource
    private PasswordEncoder passwordEncoder;


    @PostMapping("login")
    public void doLogin(@Validated @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            //登录认证
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //构造token信息
            String ip = AccessAddressUtils.getIpAddress(request);
            LoginUser userDetails = (LoginUser) authentication.getPrincipal();
            log.info("登录IP：{}", ip);
            userDetails.setIp(ip);
            String jwtToken = JWTTokenUtils.createAccessToken(userDetails);

            JWTTokenUtils.setTokenInfo(jwtToken, userDetails.getUsername(), ip);

            log.info("请求成功，登录用户为：{}，token为：{}，已保存到redis", loginDto.getUserName(), jwtToken);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", jwtToken);
            RestResult.responseJson(response, new RestResult<>(200, "登录成功", tokenMap));

        } catch (Exception e) {

            log.info(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(600, "登录失败", e.getMessage()));

        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("addUser")
    public void addUser(@RequestBody SysUser sysUser, HttpServletResponse response) {
        try {
            if (sysUser.getPassword() != null && !sysUser.getPassword().isEmpty()) {
                LoginUser loginUser = UserUtils.getCurrentUser();
                String password = passwordEncoder.encode(sysUser.getPassword());
                sysUser.setPassword(password);
                sysUser.setId(Long.valueOf(IdUtil.getSnowflakeNextIdStr()));
                sysUser.setCreateBy(loginUser.getId());
                sysUser.setCreateTime(new Date());
                if (userService.insertUser(sysUser) == 0) {
                    throw new Exception("用户名已存在");
                }
                RestResult.responseJson(response, new RestResult<>(200, "注册成功", "请于登录页面登录"));
            } else
                throw new Exception("密码为空");
        } catch (Exception e) {
            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(417, "注册失败", e.getMessage()));

        }
    }
    @PostMapping("register")
    public void doRegister(@RequestBody SysUser sysUser, HttpServletResponse response) {
        try{
        userService.doRegister(sysUser,response);}
        catch (Exception e){
            RestResult.responseJson(response, new RestResult<>(417, "注册失败", e.getMessage()));
        }
    }


    @GetMapping("code")
    public void getCode( String email, HttpServletResponse response) {
        log.info(email);
        emailService.sendCode(response,email);
    }
    @PostMapping("code")
    public void verCode( HttpServletResponse response,@RequestParam("email") String email,@RequestParam("code")String code) {
        log.info(email);
        log.info(code);
        emailService.VerCode(response,email,code);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public void userQuery(HttpServletResponse response){
        try {
            RestResult.responseJson(response, new RestResult<>(200, null, userService.listUserInfo()));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, e.getMessage(), null));
        }
    }

    @GetMapping("userInfo")
    public void userInfo(HttpServletResponse response) {

        try {
            RestResult.responseJson(response, new RestResult<>(200, null, userService.getInfo()));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, e.getMessage(), null));
        }

    }

    @PostMapping("userInfo")
    public void updateUser(HttpServletResponse response, @RequestBody UserInfo userInfo) {
        try {
            userService.updateUser(userInfo);
            RestResult.responseJson(response, new RestResult<>(200, "修改成功", null));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "修改失败", e.getMessage()));
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("userUpdate")
    public void updateUserByAdmin( @RequestBody SysUser sysUser,HttpServletResponse response) {
        try {


            userService.updateUserByAdmin(sysUser);
            RestResult.responseJson(response, new RestResult<>(200, "修改成功", null));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "修改失败", e.getMessage()));
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{userName}")
    public void deleteUser(HttpServletResponse response, @PathVariable("userName")String userName){

        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SysUser::getUserName, userName);

            Long id = userService.getOne(wrapper).getId();
            if(Objects.equals(UserUtils.getCurrentUser().getId(), id)){
                throw new Exception("不允许删除当前登录用户！！！！");
            }
            if(userService.removeById(id)){
            RestResult.responseJson(response, new RestResult<>(200, "删除成功", null));
            }else {
                RestResult.responseJson(response, new RestResult<>(604, "删除失败，查无此人", null));
            }
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "删除失败,", e.getMessage()));
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
