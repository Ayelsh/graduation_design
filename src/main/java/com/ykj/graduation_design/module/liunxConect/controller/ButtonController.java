package com.ykj.graduation_design.module.liunxConect.controller;

import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.module.liunxConect.services.ButtonServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/08/16:04
 * @Description:
 */
@RestController
@RequestMapping("/Button")
public class ButtonController {

    @Autowired
    private ButtonServices buttonServices;

    @GetMapping
    public void button(HttpServletResponse response){
        try{
            RestResult.responseJson(response, new RestResult<>(200, "成功！", buttonServices.list()));
        }catch (Exception e){
            RestResult.responseJson(response, new RestResult<>(600, "失败", e.getMessage()));
        }
    }
}
