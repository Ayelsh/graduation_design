package com.ykj.graduation_design.common.utils;

import com.ykj.graduation_design.module.login.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/08/20:27
 * @Description: 
 */
@Component
public class UserUtils {

    public static LoginUser getCurrentUser() {
        return (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
