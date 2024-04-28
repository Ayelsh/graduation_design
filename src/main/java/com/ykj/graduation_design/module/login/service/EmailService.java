package com.ykj.graduation_design.module.login.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/04/24/14:54
 * @Description:
 */
public interface EmailService {

   void sendCode(HttpServletResponse response,String email);

   void VerCode(HttpServletResponse response,String email,String code);
}
