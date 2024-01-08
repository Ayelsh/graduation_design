package com.ykj.graduation_design.module.liunxConect.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/05
 * @Description: 数据传输
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSSHData implements Serializable {

    private String operate = "connect";

    private String command = "command";

    private String host = "127.0.0.1";

    /**
     * 端口号默认为20
     */
    private Integer port = 20;
    /**
     * 登录Liunx的用户名
     */
    private String username = "test";
    /**
     * 登录Liunx的密码
     */
    private String password = "962464";


//    public WebSSHData(String operate ){this.operate = operate;}



}
