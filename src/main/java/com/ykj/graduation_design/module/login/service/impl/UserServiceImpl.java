package com.ykj.graduation_design.module.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykj.graduation_design.common.entity.User;
import com.ykj.graduation_design.module.login.mapper.UserMapper;
import com.ykj.graduation_design.module.login.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/04/10:34
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public int insertUser(User user) {
        return baseMapper.insert(user);
    }
}
