package com.ykj.graduation_design.module.login.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/05/10:26
 * @Description:
 */
@Data
public class LoginDto {
    @NotBlank(message = "用户名不得为空")
    private String userName;
    @NotBlank(message = "密码不得为空")
    private String password;
}
