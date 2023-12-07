package com.ykj.graduation_design.module.login.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ykj.graduation_design.common.utils.HttpUtil;
import com.ykj.graduation_design.common.Oauth2Properties;
import com.ykj.graduation_design.common.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth_gitee")
@Slf4j
public class oathFromGiteeController {

    @Autowired
    private Oauth2Properties oauth2Properties;

    @GetMapping("redirect")
    @ResponseBody
    public RestResult oathRedirect(@RequestParam("code") String code){

        //构造url，请求token
        log.info("code={}", code);
        String url = oauth2Properties.generateCodeUrl(code);
        JSONObject jsonObject = HttpUtil.doPost(url, "application/json");

        //构造url，请求userInfo
        String accessToken = jsonObject.getString("access_token");
        log.info("accessToken={}", accessToken);
        String msg = HttpUtil.doGetBringToken(oauth2Properties.getUserInfoUrl(), "application/json", accessToken);

        return new RestResult<>(HttpStatus.OK, msg);

    }
}
