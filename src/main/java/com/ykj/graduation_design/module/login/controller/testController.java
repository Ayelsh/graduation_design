package com.ykj.graduation_design.module.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class testController {

    @GetMapping(value = "/")
    public String index() {
        log.info(SecurityContextHolder.getContext().getAuthentication().toString());
        return "Welcome " + SecurityContextHolder.getContext().getAuthentication();
    }

}
