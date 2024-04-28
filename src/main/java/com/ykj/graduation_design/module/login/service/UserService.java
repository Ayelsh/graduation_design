package com.ykj.graduation_design.module.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.common.entity.UserInfo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/04/10:33
 * @Description:
 */
public interface UserService extends IService<SysUser> {

    int insertUser(SysUser sysUser);

    UserInfo getInfo();

    void updateUser(UserInfo userInfo);

    List<UserInfo> listUserInfo();

    void updateUserByAdmin(SysUser sysUser);

    boolean checkValueExist(String value, String columnName);

    void doRegister(SysUser sysUser, HttpServletResponse response);
}
