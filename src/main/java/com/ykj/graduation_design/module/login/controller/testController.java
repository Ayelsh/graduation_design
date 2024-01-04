package com.ykj.graduation_design.module.login.controller;

import com.ykj.graduation_design.common.RestResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class testController {

    @GetMapping(value = "/index")
    public void index(HttpServletResponse response) {
        log.info(SecurityContextHolder.getContext().getAuthentication().toString());
        RestResult.responseJson(response,new RestResult<>(HttpStatus.OK,"\"登录成功\""));
    }

}
