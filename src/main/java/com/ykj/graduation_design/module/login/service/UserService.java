package com.ykj.graduation_design.module.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykj.graduation_design.common.entity.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/04/10:33
 * @Description:
 */
public interface UserService extends IService<User> {

    int insertUser(User user);
}
